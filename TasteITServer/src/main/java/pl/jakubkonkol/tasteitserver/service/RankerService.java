package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.repository.UserActionRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankerService {
    private final UserActionRepository userActionRepository;
    private final UserService userService;

//    public List<Post> rankPosts(List<Post> candidates, String sessionToken) {
//        Map<String, Double> postScores = new HashMap<>();
//        for (Post candidate : candidates) {
//            double score = 0;
//            int commentCount = candidate.getComments().size();
//            int likeCount = candidate.getLikes().size();
//            score += commentCount * 3;
//            score += likeCount;
//            long addToFoodlistCount = userActionRepository.countByActionTypeAndPostId("ADD_TO_FOODLIST", candidate.getPostId());
//            score += addToFoodlistCount;
//            UserReturnDto postAuthor = userService.getUserDtoById(candidate.getUserId(), sessionToken);
//            if (postAuthor.getIsFollowing()) {
//                score *= 1.2;
//            }
//            score *= calculateTimeMultiplier(candidate.getCreatedDate());
//            postScores.put(candidate.getPostId(), score);
//        }
//        return candidates.stream()
//                .sorted((post1, post2) -> Double.compare(
//                        postScores.getOrDefault(post2.getPostId(), 0.0),
//                        postScores.getOrDefault(post1.getPostId(), 0.0)))
//                .collect(Collectors.toList());
//    }
//
//    private double calculateTimeMultiplier(Date createdDate) {
//        long hoursSinceCreation = Duration.between(createdDate.toInstant(), Instant.now()).toHours();
//
//        if (hoursSinceCreation <= 1) {
//            return 2.0;
//        } else if (hoursSinceCreation <= 3) {
//            return 1.6;
//        } else if (hoursSinceCreation <= 6) {
//            return 1.4;
//        } else if (hoursSinceCreation <= 24) {
//            return 1.2;
//        } else {
//            return 1.0;
//        }
//    }
}
