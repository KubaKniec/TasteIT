package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Like;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.repository.LikeRepository;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public void likePost(String postId, String token) {
        UserReturnDto userByToken = userService.getUserByToken(token);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Like like = Like.builder()
                .postId(post.getPostId())
                .userId(userByToken.getUserId())
                .build();

        likeRepository.save(like);
        post.getLikes().add(like);
        postRepository.save(post);
    }

    public void unlikePost(String postId, String token) {
        UserReturnDto userByToken = userService.getUserByToken(token);
        Like like = likeRepository.findByPostIdAndUserId(postId, userByToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        post.getLikes().remove(like);
        postRepository.save(post);
        likeRepository.delete(like);
    }
}
