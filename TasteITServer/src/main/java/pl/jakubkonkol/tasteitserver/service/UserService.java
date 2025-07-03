package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import pl.jakubkonkol.tasteitserver.dto.*;
import pl.jakubkonkol.tasteitserver.model.*;
import pl.jakubkonkol.tasteitserver.event.PreferenceUpdateRequiredEvent;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Ingredient;
import pl.jakubkonkol.tasteitserver.model.Tag;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.model.UserAction;
import pl.jakubkonkol.tasteitserver.model.enums.NotificationType;
import pl.jakubkonkol.tasteitserver.model.projection.UserProfileView;
import pl.jakubkonkol.tasteitserver.model.projection.UserShort;
import pl.jakubkonkol.tasteitserver.repository.CommentRepository;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;
import pl.jakubkonkol.tasteitserver.repository.UserActionRepository;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;
import pl.jakubkonkol.tasteitserver.service.interfaces.IPostRankingService;
import pl.jakubkonkol.tasteitserver.service.interfaces.IPostValidationService;
import pl.jakubkonkol.tasteitserver.service.interfaces.IUserService;
import pl.jakubkonkol.tasteitserver.model.enums.PreferenceUpdateReason;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final IngredientService ingredientService;
    private final TagService tagService;
    private final UserActionRepository userActionRepository;
    private final IPostValidationService postValidationService;
    private final NotificationEventPublisher notificationEventPublisher;
    private final NewBadgeService newBadgeService;
    private final ApplicationEventPublisher eventPublisher;
    private final IPostRankingService postRankingService;

    private static final java.util.logging.Logger LOGGER = Logger.getLogger(UserService.class.getName());


    public List<UserReturnDto> getUsers() {
        List<User> users = userRepository.findAll().stream().toList();
        List<UserReturnDto> userReturnDtos = new ArrayList<>();
        for (User user : users) {
            userReturnDtos.add(modelMapper.map(user, UserReturnDto.class));
        }
        return userReturnDtos;

    }

    @Cacheable(value = "userById", key = "#userId")

    public UserReturnDto getUserDtoById(String userId, String sessionToken) {
        User user = getFullUserById(userId);
        User currentUser = getCurrentUserBySessionToken(sessionToken);
        UserReturnDto userReturnDto = convertToDto(user);
        userReturnDto.setIsFollowing(currentUser.getFollowing().contains(userId));
        List<BadgeDto> allBadges = newBadgeService.updateBadges(user);
        userReturnDto.setBadges(allBadges);
        return userReturnDto;
    }

    public User getSimpleUserById(String userId) { //return users without additional collections like his posts
        return userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    public User getFullUserById(String userId){ //returns user with additional collections from other repositories
        User user = getSimpleUserById(userId);
        user.setPosts(postRepository.findByUserId(userId));
        List<Comment> comments = commentRepository.findByUserId(userId);
        user.setCreatedComments(comments);
        return user;
    }

    public UserReturnDto getUserProfileView(String userId, String sessionToken) {
        UserProfileView userProfileView = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User currentUser = getCurrentUserBySessionToken(sessionToken);
        return convertUserProfileViewToUserReturnDto(userId, userProfileView, currentUser);
    }

    @Cacheable(value = "userBySessionToken", key = "#sessionToken")
    public UserReturnDto getCurrentUserDtoBySessionToken(String sessionToken) {
        User user = getCurrentUserBySessionToken(sessionToken);
        return convertToDto(user);
    }

    @Caching(evict = {
        @CacheEvict(value = {"userById", "userProfileView", "userShort"}, key = "#userProfileDto.userId"),
        @CacheEvict(value = "userBySessionToken", key = "#sessionToken")
    })
    public void updateUserProfile(
        UserProfileDto userProfileDto,
        String sessionToken
    ) {
        checkIfUserExists(userProfileDto.getUserId());
        userRepository.updateUserProfileFields(
                userProfileDto.getUserId(),
                userProfileDto.getDisplayName(),
                userProfileDto.getBio(),
                userProfileDto.getProfilePicture(),
                userProfileDto.getBirthDate()
        );
    }

    @Caching(evict = {
        @CacheEvict(value = {"userById", "userProfileView", "userShort"}, key = "#userId"),
        @CacheEvict(value = "userBySessionToken", key = "#sessionToken")
    })
    public void changeUserFirstLogin(String userId, String sessionToken) {
        checkIfUserExists(userId);
        userRepository.setFirstLoginToFalse(userId);
    }

    @Caching(evict = {
        @CacheEvict(value = {"userById", "userProfileView", "userShort"}, key = "#userId"),
        @CacheEvict(value = "userBySessionToken", key = "#sessionToken")
    })
    public void updateUserTags(String userId, UserTagsDto userTagsDto, String sessionToken) {
        User user = getSimpleUserById(userId);
        user.setTags(userTagsDto.getTags());
        userRepository.save(user);

        eventPublisher.publishEvent(
                new PreferenceUpdateRequiredEvent(userId, PreferenceUpdateReason.TAGS_UPDATE)
        );
        LOGGER.log(Level.INFO, "Requesting preference update for user {0} due to tags update", userId);
    }

    @CacheEvict(value = {"followers", "following"}, allEntries = true)
    public void followUser(String targetUserId, String sessionToken) {
        checkIfUserExists(targetUserId);
        User currentUser = getCurrentUserBySessionToken(sessionToken);

        if (currentUser.getUserId().equals(targetUserId)) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }

        if (!currentUser.getFollowing().contains(targetUserId)) {
            userRepository.addFollowing(currentUser.getUserId(), targetUserId);
            userRepository.addFollower(targetUserId, currentUser.getUserId());
            handleFollowNotification(currentUser.getUserId(), targetUserId);
        } else {
            throw new IllegalStateException("User is already following the target user.");
        }
    }

    @CacheEvict(value = {"followers", "following"}, allEntries = true)
    public void unfollowUser(String targetUserId, String sessionToken) {
        checkIfUserExists(targetUserId);
        User currentUser = getCurrentUserBySessionToken(sessionToken);

        if (currentUser.getUserId().equals(targetUserId)) {
            throw new IllegalArgumentException("User cannot unfollow themselves.");
        }

        if (currentUser.getFollowing().contains(targetUserId)) {
            userRepository.removeFollowing(currentUser.getUserId(), targetUserId);
            userRepository.removeFollower(targetUserId, currentUser.getUserId());
        } else {
            throw new IllegalStateException("User is not following the target user.");
        }
    }

    @Cacheable(value = "followers", key = "#userId")
    public PageDto<UserReturnDto> getFollowers(String userId, String sessionToken, Integer page, Integer size) {
        checkIfUserExists(userId);
        User currentUser = getCurrentUserBySessionToken(sessionToken);
        List<String> followers = userRepository.findFollowersByUserId(userId)
                .map(User::getFollowers)
                .orElse(new ArrayList<>());

        return getUserPage(followers, currentUser, page, size);
    }

    @Cacheable(value = "following", key = "#userId")
    public PageDto<UserReturnDto> getFollowing(String userId, String sessionToken, Integer page, Integer size) {
        checkIfUserExists(userId);
        User currentUser = getCurrentUserBySessionToken(sessionToken);
        List<String> following = userRepository.findFollowingByUserId(userId)
                .map(User::getFollowing)
                .orElse(new ArrayList<>());

        return getUserPage(following, currentUser, page, size);
    }

    private PageDto<UserReturnDto> getUserPage(List<String> userIds, User currentUser, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserShort> userPage = userRepository.findUsersByUserIdIn(userIds, pageable);
        return getUserReturnDtoPageDto(currentUser, userPage, pageable);
    }

    public PageDto<UserReturnDto> searchUsersByDisplayName(String query, String sessionToken, Integer page, Integer size) {
        User currentUser = getCurrentUserBySessionToken(sessionToken);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserShort> userPage = userRepository.findByDisplayNameContainingIgnoreCase(query, pageable);

        return getUserReturnDtoPageDto(currentUser, userPage, pageable);
    }

    private PageDto<UserReturnDto> getUserReturnDtoPageDto(User currentUser, Page<UserShort> userPage, Pageable pageable) {
        List<UserReturnDto> userDtos = userPage.getContent().stream().map(user -> {
            UserReturnDto userReturnDto = new UserReturnDto(); //todo
            userReturnDto.setUserId(user.getUserId());
            userReturnDto.setDisplayName(user.getDisplayName());
            userReturnDto.setProfilePicture(user.getProfilePicture());
            userReturnDto.setIsFollowing(currentUser.getFollowing().contains(user.getUserId()));

            return userReturnDto;
        }).toList();

        PageImpl<UserReturnDto> pageImpl = new PageImpl<>(userDtos, pageable, userPage.getTotalElements());
        return getUsersShortPageDto(pageImpl);
    }

    private PageDto<UserReturnDto> getUsersShortPageDto(PageImpl<UserReturnDto> pageImpl) {
        PageDto<UserReturnDto> pageDto = new PageDto<>();
        pageDto.setContent(pageImpl.getContent());
        pageDto.setPageNumber(pageImpl.getNumber());
        pageDto.setPageSize(pageImpl.getSize());
        pageDto.setTotalElements(pageImpl.getTotalElements());
        pageDto.setTotalPages(pageImpl.getTotalPages());

        return pageDto;
    }


    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }


    public User getCurrentUserBySessionToken(String sessionToken) {
        return userRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserShort getCurrentUserShortBySessionToken(String sessionToken) {
        return userRepository.findUserShortBySessionToken(sessionToken)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserReturnDto convertToDto(User user) {
        UserReturnDto userReturnDto = modelMapper.map(user, UserReturnDto.class); //todo
        userReturnDto.setFollowersCount((long) user.getFollowers().size());
        userReturnDto.setFollowingCount((long) user.getFollowing().size());

        return userReturnDto;
    }

    private UserProfileDto convertToProfileDto(User user) {
        return modelMapper.map(user, UserProfileDto.class);
    }

    private User convertProfileToEntity(UserProfileDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public void checkIfUserExists(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with id " + userId + " does not exist.");
        }
    }

    @CacheEvict(value = {"userShort", "userById", "userBySessionToken", "userShort"}, allEntries = true)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    private UserReturnDto convertUserProfileViewToUserReturnDto(String userId, UserProfileView userProfileView, User currentUser) {
        UserReturnDto userReturnDto = new UserReturnDto(); //todo
        userReturnDto.setUserId(userProfileView.getUserId());
        userReturnDto.setEmail(userProfileView.getEmail());
        userReturnDto.setDisplayName(userProfileView.getDisplayName());
        userReturnDto.setBio(userProfileView.getBio());
        userReturnDto.setProfilePicture(userProfileView.getProfilePicture());
        userReturnDto.setCreatedAt(userProfileView.getCreatedAt());
        userReturnDto.setBirthDate(userProfileView.getBirthDate());
        userReturnDto.setFirstLogin(userProfileView.getFirstLogin());
        userReturnDto.setRoles(userProfileView.getRoles());
        userReturnDto.setFollowersCount((long) userProfileView.getFollowers().size());
        userReturnDto.setFollowingCount((long) userProfileView.getFollowing().size());
        Long postsCount = postRepository.countByUserId(userId);
        userReturnDto.setPostsCount(postsCount);
        userReturnDto.setIsFollowing(currentUser.getFollowing().contains(userId));

        return userReturnDto;
    }

    public List<UserShort> getUserShortByIdIn(List<String> userIds) {
        return userRepository.findUsersByUserIdIn(userIds);
    }

    public UserShort findUserShortByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findUserShortByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? (String) authentication.getDetails() : null;
    }

    public void updateUserBannedIngredients(String sessionToken, List<IngredientDto> ingredients) {
        User user = getCurrentUserBySessionToken(sessionToken);
        List<Ingredient> bannedIngredients = ingredients.stream()
                .map(ingredientService::convertToEntity)
                .toList();
        user.setBannedIngredients(bannedIngredients);
        userRepository.save(user);
        postRankingService.clearRankedPostsCacheForUser(user.getUserId());
    }

    public void updateUserBannedTags(String sessionToken, List<TagDto> tags) {
        User user = getCurrentUserBySessionToken(sessionToken);
        List<Tag> bannedTags = tags.stream()
                .map(tagService::convertToEntity)
                .toList();
        user.setBannedTags(bannedTags);
        userRepository.save(user);
        postRankingService.clearRankedPostsCacheForUser(user.getUserId());
    }

    public List<IngredientDto> getUserBannedIngredients(String sessionToken) {
        User user = getCurrentUserBySessionToken(sessionToken);

        return user.getBannedIngredients().stream()
                .map(ingredientService::convertToDto)
                .toList();
    }

    public List<TagDto> getUserBannedTags(String sessionToken) {
        User user = getCurrentUserBySessionToken(sessionToken);

        return user.getBannedTags().stream()
                .map(tagService::convertToDto)
                .toList();
    }

    public void updateUserBadges(String userId, List<Badge> updatedBadges) { 
        checkIfUserExists(userId);
        userRepository.updateUserBadgesByUserId(userId, updatedBadges);
    }

    public List<User> findUsersActiveInLast30Days() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<String> activeUserIds = userActionRepository
                .findByTimestampAfter(thirtyDaysAgo)
                .stream()
                .map(UserAction::getUserId)
                .distinct()
                .toList();

        return userRepository.findAllById(activeUserIds);
    }

    @CacheEvict(value = {"users", "userById"}, allEntries = true)
    public void deleteUser(String email, String sessionToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        
        // Delete all user posts
        if (user.getPosts() != null) {
            user.getPosts().forEach(post -> {
                try {
                    postValidationService.deletePost(post.getPostId(), sessionToken);
                } catch (Exception e) {
                    // Log error but continue with deletion
                    LOGGER.log(Level.SEVERE,"Error deleting post " + post.getPostId() + ": " + e.getMessage());
                }
            });
        }
        
        // Delete user
        userRepository.delete(user);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    private void handleFollowNotification(String followerId, String targetUserId) {
        try {
            notificationEventPublisher.publishNotification(
                    NotificationType.NEW_FOLLOWER,
                    targetUserId,
                    followerId,
                    null
            );
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,"Failed to send follow notification", e);
        }
    }


}

