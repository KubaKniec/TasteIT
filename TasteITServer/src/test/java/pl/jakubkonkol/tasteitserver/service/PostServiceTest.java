package pl.jakubkonkol.tasteitserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.jakubkonkol.tasteitserver.dto.PageDto;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.PostMedia;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.model.projection.PostPhotoView;
import pl.jakubkonkol.tasteitserver.model.projection.UserShort;
import pl.jakubkonkol.tasteitserver.repository.LikeRepository;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;
import pl.jakubkonkol.tasteitserver.service.PostService;
import pl.jakubkonkol.tasteitserver.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private PostService postService;

    private static final String TEST_POST_ID = "test-post-1";
    private static final String TEST_USER_ID = "test-user-1";
    private static final String TEST_SESSION_TOKEN = "test-session-token";

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, modelMapper, postRepository, null, null, null);
        postService = new PostService(userService, likeRepository, postRepository, modelMapper, null);
    }

    @Test
    @DisplayName("Powinno zwrócić post po ID")
    void shouldReturnPostById() {
        // Given
        Post post = createTestPost();
        User user= createTestUser();
        UserShort userShort = createTestUserShort();

        when(postRepository.findById(TEST_POST_ID)).thenReturn(Optional.of(post));
        when(modelMapper.map(post, PostDto.class)).thenReturn(createTestPostDto());
        when(userRepository.findBySessionToken(TEST_SESSION_TOKEN)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserReturnDto.class)).thenReturn(createTestUserReturnDto());
        when(userRepository.findUserShortByUserId(TEST_USER_ID)).thenReturn(Optional.of(userShort));

        // When
        PostDto result = postService.getPost(TEST_POST_ID, TEST_SESSION_TOKEN);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(TEST_POST_ID);
    }

    @Test
    @DisplayName("Powinno zgłosić wyjątek gdy post nie istnieje")
    void shouldThrowExceptionWhenPostNotFound() {
        // Given
        when(postRepository.findById(TEST_POST_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> {
            postService.getPost(TEST_POST_ID, TEST_SESSION_TOKEN);
        });
    }

    @Test
    @DisplayName("Powinno zwrócić posty użytkownika")
    void shouldReturnUserPosts() {
        // Given
        int page = 0;
        int size = 10;
        PostPhotoView postPhotoView = mock(PostPhotoView.class);
        List<PostPhotoView> postPhotoViews = List.of(postPhotoView);
        Page<PostPhotoView> postPage = new PageImpl<>(postPhotoViews, PageRequest.of(page, size), 1);

        when(userRepository.existsById(TEST_USER_ID)).thenReturn(true);
        when(postRepository.findPostsByUserId(TEST_USER_ID, PageRequest.of(page, size)))
                .thenReturn(postPage);

        // When
        PageDto<PostDto> result = postService.getUserPosts(TEST_USER_ID, page, size);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(postRepository).findPostsByUserId(TEST_USER_ID, PageRequest.of(page, size));
        verify(userRepository).existsById(TEST_USER_ID);
    }

    private Post createTestPost() {
        Post post = new Post();
        post.setPostId(TEST_POST_ID);
        post.setUserId(TEST_USER_ID);

        PostMedia postMedia = new PostMedia();
        postMedia.setTitle("Test Post");
        postMedia.setPictures(List.of("test-picture-url"));

        post.setPostMedia(postMedia);
        return post;
    }

    private PostDto createTestPostDto() {
        PostDto dto = new PostDto();
        dto.setPostId(TEST_POST_ID);
        return dto;
    }

    private User createTestUser() {
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setDisplayName("Test User");
        return user;
    }

    private UserReturnDto createTestUserReturnDto() {
        UserReturnDto dto = new UserReturnDto();
        dto.setUserId(TEST_USER_ID);
        dto.setDisplayName("Test User");
        return dto;
    }

    private UserShort createTestUserShort() {
        return new UserShort() {
            @Override
            public String getUserId() {
                return "";
            }

            @Override
            public String getDisplayName() {
                return "";
            }

            @Override
            public String getProfilePicture() {
                return "";
            }
        };
    }
}