package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.model.Cluster;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.model.UserAction;
import pl.jakubkonkol.tasteitserver.repository.ClusterRepository;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPreferencesAnalysisService {
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final ClusterRepository clusterRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final static String PREFERENCE_GROUP =  "preference-group";
    private final static String PREFERENCE_TOPIC = "preference-analysis-response";
    private static final Logger LOGGER = Logger.getLogger(UserPreferencesAnalysisService.class.getName());

    public void requestPreferenceAnalysis(String userId) {
        String correlationId = UUID.randomUUID().toString();

        // Pobierz akcje użytkownika z ostatnich X dni
        // Starsze akcje możnaby ususwać, jeżeli są jakieś nowe
        Date startDate = Date.from(LocalDateTime.now().minusDays(30).toInstant(ZoneOffset.UTC));
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId)
                .and("timestamp").gte(startDate));

        List<UserAction> userActions = mongoTemplate.find(query, UserAction.class);

        // Przygotuj payload z akcjami
        Map<String, Object> userData = prepareUserDataWithActions(userId, userActions);
        Map<String, Object> clustersData = prepareClusterData();

        Map<String, Object> payload = new HashMap<>();
        payload.put("userData", userData);
        payload.put("clustersData", clustersData);
        payload.put("correlationId", correlationId);

        kafkaTemplate.send("preference-analysis-request", correlationId, payload);
    }

    /**
     *
     * Zwraca strukture:
     * {
     *     "actionType": "LIKE_POST",
     *     "timestamp": "2024-12-01T10:31:13.112Z",
     *     "metadata": {
     *         "postId": "674b97b2659ea4687a17e710",
     *         "post": {
     *             // pełne dane posta
     *         }
     *     }
     * }
     */
    private Map<String, Object> prepareUserDataWithActions(String userId, List<UserAction> actions) {
        User user = userService.getUserById(userId);

        Set<String> postIds = actions.stream()
                .map(action -> action.getMetadata().get("postId").toString())
                .collect(Collectors.toSet());

        Map<String, Post> postsMap = postRepository.findAllById(postIds).stream()
                .collect(Collectors.toMap(Post::getPostId, post -> post));

        List<Map<String, Object>> enrichedActions = actions.stream()
                .map(action -> {
                    Map<String, Object> enrichedAction = new HashMap<>();
                    enrichedAction.put("actionType", action.getActionType());
                    enrichedAction.put("timestamp", action.getTimestamp());

                    Map<String, Object> enrichedMetadata = new HashMap<>(action.getMetadata());
                    String postId = action.getMetadata().get("postId").toString();

                    if (postsMap.containsKey(postId)) {
                        Post post = postsMap.get(postId);
                        Map<String, Object> postData = new HashMap<>();

                        // Dodaj podstawowe informacje o poście
                        postData.put("postMedia", post.getPostMedia());
                        postData.put("tags", post.getTags());

                        // Dodaj informacje o składnikach
                        if (post.getRecipe() != null) {
                            postData.put("recipe", Map.of(
                                    "ingredientsWithMeasurements", post.getRecipe().getIngredientsWithMeasurements()
                            ));
                        }

                        // Dodaj referencje do klastrów (używając ObjectId)
                        postData.put("clusters", post.getClusters());

                        enrichedMetadata.put("post", postData);
                    }
                    enrichedAction.put("metadata", enrichedMetadata);
                    return enrichedAction;
                })
                .toList();

        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("tags", user.getTags());
        userData.put("actions", enrichedActions);
        return userData;
    }

    private Map<String, Object> prepareClusterData() {
        Map<String, Object> clustersData = new HashMap<>();
        List<Cluster> clusters = clusterRepository.findAll();

        for (Cluster cluster : clusters) {
            Map<String, Object> clusterInfo = new HashMap<>();
            clusterInfo.put("name", cluster.getName());
            clusterInfo.put("main_topics", cluster.getMainTopics());
            clusterInfo.put("keyword_weights", cluster.getKeywordWeights());
            clustersData.put(cluster.getClusterId(), clusterInfo);
        }

        return clustersData;
    }

    @Transactional
    @KafkaListener(
            topics = PREFERENCE_TOPIC,
            groupId = PREFERENCE_GROUP
    )
    public void handlePreferenceAnalysisResponse(@Payload Map<String, Object> response) {
        String correlationId = (String) response.get("correlationId");
        LOGGER.log(Level.INFO, "Received preference analysis response for correlationId: " + correlationId);

        try {
            if ("success".equals(response.get("status"))) {
                @SuppressWarnings("unchecked")
                Map<String, Object> userPreferences = (Map<String, Object>) response.get("userPreferences");
                String userId = (String) userPreferences.get("user_id");

                LOGGER.log(Level.INFO, "Processing preferences for user: " + userId);

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> matchedClusters =
                        (List<Map<String, Object>>) userPreferences.get("matched_clusters");

                if (userId != null && matchedClusters != null) {
                    User user = userService.getUserById(userId);
                    Map<String, Double> clusterPreferences = new HashMap<>();

                    // Przetwórz dopasowane klastry i ich wagi
                    for (Map<String, Object> cluster : matchedClusters) {
                        String clusterId = (String) cluster.get("cluster_id");
                        Double score = ((Number) cluster.get("similarity_score")).doubleValue();

                        // Znajdź ObjectId klastra
                        clusterRepository.findByClusterId(clusterId).ifPresent(foundCluster -> clusterPreferences.put(foundCluster.getId(), score));
                    }

                    // Zaktualizuj preferencje użytkownika
                    user.setClusterPreferences(clusterPreferences);
                    userRepository.save(user);

                    LOGGER.log(Level.INFO, "Updated user cluster preferences with weights");
                }
            } else {
                LOGGER.log(Level.WARNING, "Error in preference analysis: " + response.get("message"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error processing preference analysis response: " + e.getMessage());
        }
    }
}