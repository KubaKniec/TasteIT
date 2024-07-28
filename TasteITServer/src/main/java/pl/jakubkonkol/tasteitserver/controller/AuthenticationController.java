package pl.jakubkonkol.tasteitserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubkonkol.tasteitserver.dto.UserCreationRequestDto;
import pl.jakubkonkol.tasteitserver.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody final UserCreationRequestDto userCreationRequest) {
        authenticationService.register(userCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
