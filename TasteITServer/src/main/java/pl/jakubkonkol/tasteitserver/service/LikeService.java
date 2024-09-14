package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Like;
import pl.jakubkonkol.tasteitserver.repository.LikeRepository;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    public void likePost(String postId, String token) {
        UserReturnDto userByToken = userService.getUserByToken(token);
        PostDto post = postService.getPost(postId);

        Like like = Like.builder()
                .postId(post.getPostId())
                .userId(userByToken.getUserId())
                .build();

        likeRepository.save(like);
    }

    public void unlikePost(String postId, String token) {
        UserReturnDto userByToken = userService.getUserByToken(token);
        Like like = likeRepository.findByPostIdAndUserId(postId, userByToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));

        likeRepository.delete(like);
    }
}
