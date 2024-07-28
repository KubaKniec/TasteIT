package pl.jakubkonkol.tasteitserver.client;

import org.springframework.stereotype.Component;
import pl.jakubkonkol.tasteitserver.dto.TokenSuccessResponseDto;
import pl.jakubkonkol.tasteitserver.dto.UserLoginRequestDto;

@Component

public class FirebaseAuthClient {
//    public TokenSuccessResponseDto login(final UserLoginRequestDto userLoginRequest) {
//        final var requestBody = prepareRequestBody(userLoginRequest);
//        final var response = sendSignInRequest(requestBody);
//        return TokenSuccessResponseDto.builder()
//                .accessToken(response.getIdToken())
//                .build();
//    }
}
