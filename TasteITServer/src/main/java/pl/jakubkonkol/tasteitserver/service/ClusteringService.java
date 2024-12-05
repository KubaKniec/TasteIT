package pl.jakubkonkol.tasteitserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Cluster;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.repository.ClusterRepository;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClusteringService {
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ClusterRepository clusterRepository;
    private final static String CLUSTERING_GROUP =  "clustering-group";
    private final static String CLUSTERING_TOPIC = "clustering-response";
    private static final Logger LOGGER = Logger.getLogger(ClusteringService.class.getName());

    public void requestClustering() {
        String correlationId = UUID.randomUUID().toString();

        List<PostDto> posts = postRepository.findAll().stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();

        Map<String, Object> payload = new HashMap<>();
        payload.put("posts", posts);
        payload.put("correlationId", correlationId);

        String requestTopic = "clustering-request";
        kafkaTemplate.send(requestTopic, correlationId, payload);
    }

    @Transactional
    @KafkaListener(
            topics = CLUSTERING_TOPIC,
            groupId = CLUSTERING_GROUP
    )
    public void handleClusteringResponse(@Payload Map<String, Object> response) {
        String correlationId = (String) response.get("correlationId");
        LOGGER.log(Level.INFO, "Received response for correlationId: " + correlationId);

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> clustersData = (Map<String, Map<String, Object>>) response.get("clusters");
        createClusters(clustersData);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> postsAssignments = (List<Map<String, Object>>) response.get("posts_assignments");
        assignClusterToPosts(postsAssignments);

        LOGGER.log(Level.INFO, "Clusters and posts updated successfully.");
    }

    private void createClusters(Map<String, Map<String, Object>> clustersData) {
        try {
            Map<String, Cluster> existingClusters = clusterRepository.findAll().stream()
                    .collect(Collectors.toMap(Cluster::getClusterId, cluster -> cluster));

            List<Cluster> clustersToSave = clustersData.entrySet().stream()
                    .map(entry -> {
                        String clusterId = entry.getKey();
                        Map<String, Object> clusterInfo = entry.getValue();
                        return createOrUpdateCluster(clusterInfo, clusterId, existingClusters);
                    })
                    .toList();

            clusterRepository.saveAll(clustersToSave);
        } catch (Exception e) {
            throw new RuntimeException("Error creating/updating clusters", e);
        }
    }

    private Cluster createOrUpdateCluster(Map<String, Object> clusterInfo, String clusterId, Map<String, Cluster> existingClusters) {
        Cluster newCluster = convertResponseToCluster(clusterInfo);
        newCluster.setClusterId(clusterId);

        if (existingClusters.containsKey(clusterId)) {
            Cluster existingCluster = existingClusters.get(clusterId);
            newCluster.setId(existingCluster.getId());
        }

        return newCluster;
    }

    // SIDE NOTE: If clusters and posts do not change often, we may consider caching them to reduce the number of queries
    private void assignClusterToPosts(List<Map<String, Object>> postsAssignments) {
        try {
            Set<String> postIds = postsAssignments.stream()
                    .map(assignment -> assignment.get("post_id").toString())
                    .collect(Collectors.toSet());

            Set<String> newClusterIds = postsAssignments.stream()
                    .map(assignment -> assignment.get("cluster_id").toString())
                    .collect(Collectors.toSet());

            Map<String, Post> posts = postRepository.findAllById(postIds).stream()
                    .collect(Collectors.toMap(Post::getPostId, post -> post));

            Map<String, Cluster> availableClusters = clusterRepository.findByClusterIdIn(newClusterIds).stream()
                    .collect(Collectors.toMap(Cluster::getClusterId, cluster -> cluster));

            assignNewClustersToPosts(postsAssignments, posts, availableClusters);

            postRepository.saveAll(posts.values());
        } catch (Exception e) {
            throw new RuntimeException("Error assigning clusters to posts", e);
        }
    }

    private void assignNewClustersToPosts(List<Map<String, Object>> postsAssignments, Map<String, Post> posts, Map<String, Cluster> availableClusters) {
        postsAssignments.forEach(assignment -> {
            String postId = assignment.get("post_id").toString();
            String clusterId = assignment.get("cluster_id").toString();

            Post post = posts.get(postId);
            if (post != null) {
                Cluster cluster = availableClusters.get(clusterId);
                if (cluster != null && !post.getClusters().contains(cluster)) {
                    post.getClusters().add(cluster);
                }
            }
        });
    }

    private Cluster convertResponseToCluster(Map<String, Object> clusterInfo) {
        Cluster cluster = modelMapper.map(clusterInfo, Cluster.class);
        cluster.setMainTopics((List<String>) clusterInfo.get("main_topics"));
        Map<String, Double> keywordWeights = ((Map<?, ?>) clusterInfo.get("keyword_weights"))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> Double.valueOf(e.getValue().toString())
                ));
        cluster.setKeywordWeights(keywordWeights);
        return cluster;
    }
}