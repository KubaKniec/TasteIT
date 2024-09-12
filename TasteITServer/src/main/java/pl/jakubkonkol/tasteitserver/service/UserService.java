package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.IngredientDto;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.dto.UserProfileDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.model.Ingredient;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    public UserReturnDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Post with id " + userId + " not found"));
        return convertToDto(user);
    }

    public UserReturnDto getUserByToken(String sessionToken) {
        Optional<User> user = userRepository.findBySessionToken(sessionToken);
        if (user.isPresent()) {
            return convertToDto(user.get());
        }
        throw new NoSuchElementException("User with token " + sessionToken + " not found");
    }

    public UserReturnDto updateUserProfile(
            String userId,
            String newDisplayName,
            String newBio,
            String newProfilePicture,
            LocalDate newBirthDate
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(
                        "User with email " + userId + " not found"));

        user.setDisplayName(newDisplayName);
        user.setBio(newBio);
        user.setProfilePicture(newProfilePicture);
        user.setBirthDate(newBirthDate);

        userRepository.save(user);
        return convertToDto(user);
    }

    public UserReturnDto changeUserFirstLogin(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(
                        "User with email " + userId + " not found"));

        user.setFirstLogin(false);

        userRepository.save(user);
        return convertToDto(user);

    }

    private UserReturnDto convertToDto(User user) {
        return modelMapper.map(user, UserReturnDto.class);
    }

}

