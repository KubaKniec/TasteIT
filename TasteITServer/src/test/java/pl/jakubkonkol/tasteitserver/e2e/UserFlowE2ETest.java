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

import java.util.Date;
import java.util.List;

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

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class UserFlowE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String sessionToken;
    private static String userId;
    private static String postId;

    private static final String TEST_PASSWORD = "Test123!@#"; // Hasło spełniające wymagania walidacji

    @Test
    @Order(1)
    @DisplayName("Should register user")
    void shouldRegisterUser() throws Exception {
        // Given
        UserCreationRequestDto requestDto = new UserCreationRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("Test123!@#");

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        User user = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        userId = user.getUserId();
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
        // Given
        PostDto postDto = new PostDto();
        PostMedia postMedia = new PostMedia();
        postMedia.setTitle("Test post");
        postMedia.setPictures(List.of("https://example.com/image.jpg"));
        postMedia.setDescription("Test description");
        postDto.setPostMedia(postMedia);
        
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
        // When & Then
        MvcResult result = mockMvc.perform(get("/api/v1/post/" + postId)
                .header("Authorization", sessionToken))
                .andExpect(status().isOk())
                .andReturn();

        PostDto post = objectMapper.readValue(result.getResponse().getContentAsString(), PostDto.class);
        assertThat(post.getLikesCount()).isEqualTo(1);
        assertThat(post.getCommentsCount()).isEqualTo(1);
        assertThat(post.getLikedByCurrentUser()).isTrue();
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        assumeTrue(sessionToken != null || 
                  testInfo.getTestMethod().get().getName().equals("shouldRegisterUser") || 
                  testInfo.getTestMethod().get().getName().equals("shouldLoginUser"),
                  "Session token is required for this test");
                  
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
} 