package pl.jakubkonkol.tasteitserver.e2e;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.jakubkonkol.tasteitserver.dto.*;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.model.PostMedia;
import pl.jakubkonkol.tasteitserver.model.enums.PostType;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import pl.jakubkonkol.tasteitserver.service.PostService;
import pl.jakubkonkol.tasteitserver.service.CommentService;
import pl.jakubkonkol.tasteitserver.service.LikeService;
import pl.jakubkonkol.tasteitserver.service.UserService;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class UserFlowE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private static String sessionToken;
    private static String userId;
    private static String postId;
    private static String adminSessionToken;

    private static final String TEST_PASSWORD = "Test123!@#"; // Hasło spełniające wymagania walidacji

    @Test
    @Order(1)
    @DisplayName("Should register user")
    void shouldRegisterUser() throws Exception {
        // Given
        UserCreationRequestDto requestDto = new UserCreationRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword(TEST_PASSWORD);

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Użyj JsonNode zamiast bezpośredniej deserializacji do User
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        userId = jsonNode.get("userId").asText();
        assertThat(userId).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("Should login user")
    void shouldLoginUser() throws Exception {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("Test123!@#");

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        AuthenticationSuccessTokenDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), 
                AuthenticationSuccessTokenDto.class
        );
        sessionToken = response.getSessionToken();
        assertThat(sessionToken).isNotNull();
    }

    @Test
    @Order(3)
    @DisplayName("Should update user profile")
    void shouldUpdateUserProfile() throws Exception {
        assumeTrue(sessionToken != null, "Session token is required for this test");
        
        // Given
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setUserId(userId);
        profileDto.setDisplayName("Test User");
        profileDto.setBio("Test bio");
        profileDto.setProfilePicture("https://example.com/picture.jpg");
        profileDto.setBirthDate(new Date());

        // When & Then
        mockMvc.perform(put("/api/v1/user")
                .header("Authorization", sessionToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Should get feed")
    void shouldGetFeed() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/post/feed")
                .header("Authorization", sessionToken)
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    @Order(5)
    @DisplayName("Should create post")
    void shouldCreatePost() throws Exception {
        // Debug - wyświetl dostępne typy
        System.out.println("Dostępne typy postów: " + Arrays.toString(PostType.values()));
        
        // Given
        PostDto postDto = new PostDto();
        PostMedia postMedia = new PostMedia();
        postMedia.setTitle("Test post");
        postMedia.setPictures(List.of("https://example.com/image.jpg"));
        postMedia.setDescription("Test description");
        postDto.setPostMedia(postMedia);
        postDto.setPostType(PostType.FOOD);  // Zmieniamy na FOOD, bo to jedna z dostępnych wartości
        
        PostAuthorDto authorDto = new PostAuthorDto();
        authorDto.setUserId(userId);
        postDto.setPostAuthorDto(authorDto);

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/v1/post/create")
                .header("Authorization", sessionToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andReturn();

        PostDto createdPost = objectMapper.readValue(result.getResponse().getContentAsString(), PostDto.class);
        postId = createdPost.getPostId();
        assertThat(postId).isNotNull();
    }

    @Test
    @Order(6)
    @DisplayName("Should like post")
    void shouldLikePost() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/post/" + postId + "/like")
                .header("Authorization", sessionToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @DisplayName("Should add comment to post")
    void shouldAddComment() throws Exception {
        // Given
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("Test comment");

        // When & Then
        mockMvc.perform(post("/api/v1/post/" + postId + "/comment")
                .header("Authorization", sessionToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test comment"));
    }

    @Test
    @Order(8)
    @DisplayName("Should verify post interactions")
    void shouldVerifyPostInteractions() throws Exception {
        // Given
        PostDto postDto = new PostDto();
        PostMedia postMedia = new PostMedia();
        postMedia.setTitle("Test Post");
        postMedia.setPictures(List.of("test-picture-url"));
        postMedia.setDescription("Test Description");
        postDto.setPostMedia(postMedia);
        postDto.setPostType(PostType.FOOD);
        
        PostAuthorDto authorDto = new PostAuthorDto();
        authorDto.setUserId(userId);
        postDto.setPostAuthorDto(authorDto);

        // When - Create post
        MvcResult createResult = mockMvc.perform(post("/api/v1/post/create")
                .header("Authorization", sessionToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract post ID from response
        String postId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("postId")
                .asText();

        // Then - Get post and verify
        mockMvc.perform(get("/api/v1/post/" + postId)
                .header("Authorization", sessionToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.postMedia.title").value("Test Post"))
                .andExpect(jsonPath("$.postType").value("FOOD"));

        // Cleanup
        mockMvc.perform(delete("/api/v1/post/" + postId)
                .header("Authorization", sessionToken))
                .andExpect(status().isOk());
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws Exception {
        assumeTrue(sessionToken != null || 
                  testInfo.getTestMethod().get().getName().equals("shouldRegisterUser") || 
                  testInfo.getTestMethod().get().getName().equals("shouldLoginUser"),
                  "Session token is required for this test");
                  
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        
        // Clear test user before registration test
        if (testInfo.getTestMethod().get().getName().equals("shouldRegisterUser")) {
            cleanupTestUser();
        }
    }

    private void cleanupTestUser() {
        String adminSessionToken = null;  // Zmieniamy typ na String
        try {
            // Zaloguj się jako admin
            UserLoginRequestDto adminLogin = new UserLoginRequestDto();
            adminLogin.setEmail("tasteit@admin.com");
            adminLogin.setPassword(System.getenv("MONGO_PASSWORD"));

            MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(adminLogin)))
                    .andReturn();

            if (loginResult.getResponse().getStatus() == 200) {
                JsonNode loginResponse = objectMapper.readTree(loginResult.getResponse().getContentAsString());
                adminSessionToken = loginResponse.get("sessionToken").asText();
            }

            // Reszta kodu czyszczenia...
            if (postId != null) {
                // Usuń komentarze posta
                List<CommentDto> comments = commentService.getComments(postId);
                comments.forEach(comment -> {
                    try {
                        commentService.deleteComment(postId, comment.getCommentId(), sessionToken);
                    } catch (Exception e) {
                        System.err.println("Error deleting comment: " + e.getMessage());
                    }
                });

                // Usuń polubienie posta
                try {
                    likeService.unlikePost(postId, sessionToken);
                } catch (Exception e) {
                    System.err.println("Error unliking post: " + e.getMessage());
                }

                // Usuń post testowy
                try {
                    postService.deletePost(postId, sessionToken);
                } catch (Exception e) {
                    System.err.println("Error deleting post: " + e.getMessage());
                }
            }

            // Usuń użytkownika testowego
            if (userId != null) {
                try {
                    if (adminSessionToken != null) {
                        userService.deleteUser("test@example.com", adminSessionToken);
                    } else {
                        // Fallback na repozytorium
                        userRepository.deleteByEmail("test@example.com");
                    }
                } catch (Exception e) {
                    System.err.println("Error deleting user: " + e.getMessage());
                    try {
                        userRepository.deleteByEmail("test@example.com");
                    } catch (Exception ex) {
                        System.err.println("Error during repository cleanup: " + ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }

    @AfterAll
    static void cleanup(@Autowired PostService postService,
                       @Autowired CommentService commentService,
                       @Autowired LikeService likeService,
                       @Autowired MockMvc mockMvc,
                       @Autowired ObjectMapper objectMapper,
                       @Autowired UserRepository userRepository,
                       @Autowired UserService userService) {
        try {
            if (postId != null) {
                // Usuń komentarze posta
                List<CommentDto> comments = commentService.getComments(postId);
                comments.forEach(comment -> {
                    try {
                        commentService.deleteComment(postId, comment.getCommentId(), sessionToken);
                    } catch (Exception e) {
                        System.err.println("Error deleting comment: " + e.getMessage());
                    }
                });

                // Usuń polubienie posta
                try {
                    likeService.unlikePost(postId, sessionToken);
                } catch (Exception e) {
                    System.err.println("Error unliking post: " + e.getMessage());
                }

                // Usuń post testowy
                try {
                    postService.deletePost(postId, sessionToken);
                } catch (Exception e) {
                    System.err.println("Error deleting post: " + e.getMessage());
                }
            }

            // Usuń użytkownika testowego
            if (userId != null) {
                try {
                    if (adminSessionToken != null) {
                        userService.deleteUser("test@example.com", adminSessionToken);
                    } else {
                        userRepository.deleteByEmail("test@example.com");
                    }
                } catch (Exception e) {
                    System.err.println("Error deleting user: " + e.getMessage());
                    try {
                        userRepository.deleteByEmail("test@example.com");
                    } catch (Exception ex) {
                        System.err.println("Error during repository cleanup: " + ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
} 