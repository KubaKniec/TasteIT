package pl.jakubkonkol.tasteitserver.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.jakubkonkol.tasteitserver.dto.UserCreationRequestDto;
import pl.jakubkonkol.tasteitserver.exception.AccountAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final FirebaseAuth firebaseAuth;

    public void register(final UserCreationRequestDto userCreationRequestDto) {
        final var request = new UserRecord.CreateRequest();
        request.setEmail(userCreationRequestDto.getEmail());
        request.setPassword(userCreationRequestDto.getPassword());
        request.setEmailVerified(Boolean.TRUE);

        try {
            firebaseAuth.createUser(request);
        } catch (final FirebaseAuthException e) {
            if (e.getMessage().contains("EMAIL_EXISTS")) {
                throw new AccountAlreadyExistsException("email already in use");
            }
        }
    }
}
