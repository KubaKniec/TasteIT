package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Post with id " + userId + " not found"));
        return user;
    }

    public User updateUserProfile(
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

        return userRepository.save(user);
    }

    public User changeUserFirstLogin(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(
                        "User with email " + userId + " not found"));

        user.setFirstLogin(false);

        return userRepository.save(user);
    }

}

