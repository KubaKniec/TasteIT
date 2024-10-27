package pl.jakubkonkol.tasteitserver.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import pl.jakubkonkol.tasteitserver.apitools.IngredientFetcher;
import pl.jakubkonkol.tasteitserver.dto.UserCreationRequestDto;
import pl.jakubkonkol.tasteitserver.dto.UserProfileDto;
import pl.jakubkonkol.tasteitserver.model.Authentication;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;
import pl.jakubkonkol.tasteitserver.service.AuthenticationService;
import pl.jakubkonkol.tasteitserver.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class AdminUserFactory {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private static final Logger LOGGER = Logger.getLogger(AdminUserFactory.class.getName());
    private final List<String> roles = List.of("ADMIN", "USER");
    public void CreateAdmin() {
        if (userRepository.findByEmail("tasteit@admin.com").isEmpty()){

            //register admin account
            UserCreationRequestDto adminCreationDto = new UserCreationRequestDto();
            adminCreationDto.setEmail("tasteit@admin.com");
            adminCreationDto.setPassword(System.getenv("MONGO_PASSWORD"));
            authenticationService.register(adminCreationDto);

            // Force admin account to have userId 0 (Voodoo magic)
            var admin = userRepository.findByEmail("tasteit@admin.com").orElseThrow(() -> new NoSuchElementException(
                    "Unexpected error occurred while creating admin account"));
            var idToRemove = admin.getUserId();
            userRepository.deleteById(idToRemove);
            admin.setUserId("0");
            userRepository.save(admin);

            //set admin profile
            UserProfileDto adminProfile = new UserProfileDto();
            adminProfile.setUserId("0");
            adminProfile.setDisplayName("TasteIT");
            adminProfile.setBio("Admin of TasteIT");
            adminProfile.setProfilePicture("placeholder.jpg");
            adminProfile.setBirthDate(LocalDate.now());
            userService.updateUserProfile(adminProfile);
//            userService.changeUserFirstLogin("0");
            userRepository.findById("0").ifPresent(user -> {
                user.setRoles(roles);
                userRepository.save(user);
            });
            LOGGER.log(Level.INFO, "Admin account created");
        }else{
            LOGGER.log(Level.INFO, "Admin account already exist");
        }
    }
}
