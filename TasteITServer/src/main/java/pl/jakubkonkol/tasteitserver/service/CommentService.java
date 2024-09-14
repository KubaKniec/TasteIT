package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.CommentDto;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Comment;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.repository.CommentRepository;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final ModelMapper modelMapper;

    public CommentDto addComment(String postId, CommentDto commentDto, String token) {
        UserReturnDto userByToken = userService.getUserByToken(token);
        PostDto post = postService.getPost(postId);

        Comment comment = Comment.builder()
                .postId(post.getPostId())
                .userId(userByToken.getUserId())
                .content(commentDto.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return convertToDto(savedComment);
    }

    public void deleteComment(String postId, String commentId, String token) {
        UserReturnDto userByToken = userService.getUserByToken(token);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUserId().equals(userByToken.getUserId())) {
            throw new IllegalStateException("User can only delete his own comments");
        }

        commentRepository.delete(comment);
    }

    public List<CommentDto> getComments(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::convertToDto)
                .toList();
    }

    private CommentDto convertToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }
}
