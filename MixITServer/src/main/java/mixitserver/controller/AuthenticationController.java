package mixitserver.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mixitserver.model.additional.AuthenticationRequest;
import mixitserver.model.additional.AuthenticationResponse;
import mixitserver.model.dto.UserDto;
import mixitserver.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDto userDto) {
        AuthenticationResponse authenticationResponse = authenticationService.register(userDto);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        Cookie cookie = new Cookie("sessionToken", authenticationResponse.getToken());
        cookie.setHttpOnly(true); // Zabezpiecza przed dostępem przez skrypty klienta
        cookie.setPath("/"); // Ustaw ścieżkę, jeśli potrzebujesz (przy takiej cookie jest dostępne dla całej aplikacji)
        // cookie.setMaxAge(...); // Opcjonalnie ustaw czas życia, jak nie ustawisz to wygasa dopiero po zamknięciu przeglądarki
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged in successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Usuwa ciasteczko
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }

//    W przeglądarkach internetowych, sposób "usunięcia" ciasteczka polega na przesłaniu
//    nowego ciasteczka o tej samej nazwie, ale z ustawionym czasem wygaśnięcia na przeszłą
//    datę lub na 0. To sprawia, że przeglądarka "zapomina" oryginalne ciasteczko.

}
