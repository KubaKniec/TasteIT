package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.event.PreferenceUpdateRequiredEvent;
import pl.jakubkonkol.tasteitserver.event.UserActionEvent;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.UserAction;
import pl.jakubkonkol.tasteitserver.repository.UserActionRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserActivityAnalyzerService {
    private final UserActionRepository userActionRepository;
    // Publisher for broadcasting events across the application
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger LOGGER = Logger.getLogger(UserActivityAnalyzerService.class.getName());

    // Time window for analyzing recent user activity (default: 1 hour)
    @Value("${app.activity.analysis.time-window-hours:1}")
    private int timeWindowHours;

    // Number of actions that trigger an automatic preference update (default: 5)
    @Value("${app.activity.analysis.action-threshold:5}")
    private int actionThreshold;

    // Threshold for determining if user is focusing on specific content cluster (default: 30%)
    @Value("${app.activity.analysis.cluster-focus-threshold:0.3}")
    private double clusterFocusThreshold;

    /**
     * Main entry point for analyzing user activity.
     * Triggered whenever a user performs an action in the system.
     * Analyzes recent activity patterns and triggers preference updates if needed.
     */
    @EventListener
    public void analyzeUserActivity(UserActionEvent event) {
        try {
            String userId = event.userId();
            LocalDateTime windowStart = LocalDateTime.now().minusHours(timeWindowHours);

            // Get all user actions within the time window
            List<UserAction> recentActions = userActionRepository.findByUserIdAndTimestampAfterOrderByTimestampDesc(userId, windowStart);

            // Check if the activity pattern is significant enough to trigger an update
            if (isActivitySignificant(recentActions)) {
                LOGGER.log(Level.INFO,"Detected significant activity for user {0}", userId);
                // Broadcast event requesting preference update
                eventPublisher.publishEvent(
                        new PreferenceUpdateRequiredEvent(userId, "SIGNIFICANT_ACTIVITY")
                );
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO,"Error analyzing user activity", e);
        }
    }

    /**
     * Determines if user activity is significant enough to trigger preference update.
     * Activity is considered significant if either:
     * 1. The number of actions exceeds the threshold
     * 2. The user shows strong focus on specific content clusters
     */
    private boolean isActivitySignificant(List<UserAction> actions) {
        if (actions.size() >= actionThreshold) {
            return true;
        }

        Map<String, Double> clusterWeights = analyzeClusterFocus(actions);
        return hasSignificantClusterFocus(clusterWeights);
    }

    /**
     * Analyzes how user's actions are distributed across different content clusters.
     * Uses weighted analysis where different types of actions have different importance.
     */
    private Map<String, Double> analyzeClusterFocus(List<UserAction> actions) {
        // Local record for holding cluster interaction information
        record ClusterInteraction(String clusterId, double weight) {}

        // Transform user actions into cluster interactions with weights
        List<ClusterInteraction> allInteractions = actions.stream()
                .filter(action -> action.getMetadata().containsKey("post"))
                .flatMap(action -> {
                    Post post = (Post) action.getMetadata().get("post");
                    double weight = getActionWeight(action.getActionType());
                    return post.getClusters().stream()
                            .map(cluster -> new ClusterInteraction(cluster.getId(), weight));
                })
                .toList();

        // Calculate total weight for normalization
        double totalWeight = allInteractions.stream()
                .mapToDouble(interaction -> interaction.weight)
                .sum();

        if (totalWeight == 0) {
            return new HashMap<>();
        }

        // Create normalized weight distribution across clusters
        return allInteractions.stream()
                .collect(Collectors.groupingBy(
                        ClusterInteraction::clusterId,
                        Collectors.summingDouble(interaction ->
                                interaction.weight / totalWeight)
                ));
    }

    private double getActionWeight(String actionType) {
        return switch (actionType) {
            case "COMMENT_POST" -> 2.0;
            case "ADD_TO_FOODLIST" -> 3.0;
            default -> 1.0;
        };
    }

    /**
     * Checks if user shows significant focus on any content cluster.
     * Returns true if any cluster has weight above the threshold.
     */
    private boolean hasSignificantClusterFocus(Map<String, Double> clusterWeights) {
        return clusterWeights.values().stream()
                .anyMatch(weight -> weight >= clusterFocusThreshold);
    }
}
