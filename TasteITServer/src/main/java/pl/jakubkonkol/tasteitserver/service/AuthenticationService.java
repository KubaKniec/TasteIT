package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.auth.CryptoTools;
import pl.jakubkonkol.tasteitserver.dto.AuthenticationSuccessTokenDto;
import pl.jakubkonkol.tasteitserver.dto.UserCreationRequestDto;
import pl.jakubkonkol.tasteitserver.dto.UserLoginRequestDto;
import pl.jakubkonkol.tasteitserver.exception.AccountAlreadyExistsException;
import pl.jakubkonkol.tasteitserver.exception.AccountDoesNotExistException;
import pl.jakubkonkol.tasteitserver.exception.IncorrectPasswordException;
import pl.jakubkonkol.tasteitserver.model.Authentication;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;
import pl.jakubkonkol.tasteitserver.service.interfaces.IAuthenticationService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository userRepository;
    private final CryptoTools cryptoTools;

    @Override
    public User promote(String adminToken, String newAdminEmail) {
        return setRoles(adminToken,newAdminEmail, List.of("USER", "ADMIN"));
    }

    @Override
    public User demote(String adminToken, String newAdminEmail) {
        return setRoles(adminToken,newAdminEmail, List.of("USER"));
    }

    private User setRoles(String adminToken, String newAdminEmail, List<String> roles) {
        User admin = userRepository.findBySessionToken(adminToken).orElseThrow();
        if (!admin.getRoles().contains("ADMIN")) {
            throw new IllegalArgumentException("Only admin can promote new admins");
        }
        User newAdmin = userRepository.findByEmail(newAdminEmail).orElseThrow(()-> new NoSuchElementException("User to promote not found"));
        newAdmin.setRoles(roles);
        return userRepository.save(newAdmin);
    }

    public User register(final UserCreationRequestDto userCreationRequestDto) {
        var existingUser = userRepository.findByEmail(userCreationRequestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new AccountAlreadyExistsException("User with this email already exists");
        }

        var salt = cryptoTools.generateSalt();
        var authentication = Authentication.builder()
                .password(cryptoTools.authentication(userCreationRequestDto.getPassword(), salt))
                .salt(salt)
                .build();
        var user = new User();
        user.setEmail(userCreationRequestDto.getEmail());
        user.setAuthentication(authentication);

        return userRepository.save(user);

    }

    public AuthenticationSuccessTokenDto login(final UserLoginRequestDto userLoginRequestDto){
        var existingUser = userRepository.findByEmail(userLoginRequestDto.getEmail());
        if (existingUser.isEmpty()) {
            throw new AccountDoesNotExistException("User with this email does not exist");
        }
        var user = existingUser.get();
        var expectedHash = cryptoTools.authentication(userLoginRequestDto.getPassword(), user.getAuthentication().getSalt());
        if (!expectedHash.equals(user.getAuthentication().getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }
        var salt = cryptoTools.generateSalt();
        user.getAuthentication().setSessionToken(cryptoTools.authentication(userLoginRequestDto.getPassword(), salt));
        userRepository.save(user);
        return AuthenticationSuccessTokenDto.builder()
                .sessionToken(user.getAuthentication().getSessionToken())
                .build();
    }

    public void logout(final String sessionToken) {
        var existingUser = userRepository.findBySessionToken(sessionToken);
        if (existingUser.isEmpty()) {
            throw new AccountDoesNotExistException("User with this session token does not exist");
        }
        var user = existingUser.get();
        user.getAuthentication().setSessionToken(null);
        userRepository.save(user);
    }

}
