package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;
import pl.jakubkonkol.tasteitserver.service.interfaces.IPostCandidatesService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostCandidatesService implements IPostCandidatesService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final int TOTAL_POSTS_TARGET = 500;
    private static final int PREFERRED_POSTS_INITIAL = 350;  // Startujemy od 70%
    private static final int FOLLOWED_POSTS_INITIAL = 100;   // Startujemy od 20%
    private static final int RECENT_POSTS_LIMIT = 200;       // Limit dla postów uzupełniających

    public List<Post> collectPosts(User user) {
        Date cutoffDate = calculateCutoffDate(30);
        Set<String> collectedPostIds = new HashSet<>(); // Unikamy duplikatów
        List<Post> candidates = new ArrayList<>();

        // 1. Posty z preferowanych klastrów (główne źródło)
        List<Post> preferredPosts = getPreferredClusterPosts(user.getUserId(), cutoffDate);
        addUniquePostsToCollection(preferredPosts, candidates, collectedPostIds);

        // 2. Posty od obserwowanych użytkowników (dodatkowe źródło)
        if (!user.getFollowing().isEmpty()) {
            List<Post> followedPosts = getFollowedUsersPosts(user, cutoffDate, collectedPostIds);
            addUniquePostsToCollection(followedPosts, candidates, collectedPostIds);
        }

        // 3. Jeśli wciąż mamy za mało postów, uzupełniamy najnowszymi
        if (candidates.size() < TOTAL_POSTS_TARGET) {
            int remaining = TOTAL_POSTS_TARGET - candidates.size();
            List<Post> recentPosts = getRecentPosts(cutoffDate, collectedPostIds, remaining);
            addUniquePostsToCollection(recentPosts, candidates, collectedPostIds);
        }

        // 4. Jeśli nadal mamy za mało, zwiększamy zakres czasowy
        if (candidates.size() < TOTAL_POSTS_TARGET) {
            Date extendedCutoffDate = calculateCutoffDate(90);
            int remaining = TOTAL_POSTS_TARGET - candidates.size();
            List<Post> olderPosts = getRecentPosts(extendedCutoffDate, collectedPostIds, remaining);
            addUniquePostsToCollection(olderPosts, candidates, collectedPostIds);
        }

        return candidates;
    }

    private void addUniquePostsToCollection(List<Post> newPosts, List<Post> candidates, Set<String> collectedIds) {
        for (Post post : newPosts) {
            if (collectedIds.add(post.getPostId())) { // add() zwraca true jeśli element został dodany
                candidates.add(post);
            }
        }
    }

    private Date calculateCutoffDate(int days) {
        return Date.from(LocalDateTime.now()
                .minusDays(days)
                .toInstant(ZoneOffset.UTC));
    }

    private List<Post> getPreferredClusterPosts(String userId, Date cutoffDate) {
        Map<String, Double> preferences = userRepository.findClusterPreferencesById(userId)
                .map(User::getClusterPreferences)
                .orElse(new HashMap<>());

        if (preferences.isEmpty()) {
            return postRepository.findTop100ByOrderByCreatedDateDesc();
        }

        List<ObjectId> clusterIds = preferences.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .map(ObjectId::new)
                .toList();

        return postRepository.findByClustersAndCreatedDateAfter(
                clusterIds,
                cutoffDate,
                PageRequest.of(0, PREFERRED_POSTS_INITIAL)
        );
    }

    private List<Post> getFollowedUsersPosts(User user, Date cutoffDate, Set<String> excludePostIds) {
        return postRepository.findByUserIdInAndCreatedDateAfterAndPostIdNotIn(
                user.getFollowing(),
                cutoffDate,
                excludePostIds.stream().toList(),
                PageRequest.of(0, FOLLOWED_POSTS_INITIAL)
        );
    }

    private List<Post> getRecentPosts(Date cutoffDate, Set<String> excludePostIds, int limit) {
        return postRepository.findByCreatedDateAfterAndPostIdNotIn(
                cutoffDate,
                excludePostIds.stream().toList(),
                PageRequest.of(0, Math.min(limit, RECENT_POSTS_LIMIT))
        );
    }
}
