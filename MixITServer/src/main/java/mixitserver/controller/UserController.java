package mixitserver.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mixitserver.model.additional.AuthenticationRequest;
import mixitserver.model.additional.AuthenticationResponse;
import mixitserver.model.dto.UserDto;
import mixitserver.service.AuthenticationService;
import mixitserver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDto userDto) {
        AuthenticationResponse authenticationResponse = authenticationService.register(userDto);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        response.addCookie(new Cookie("sessionToken", authenticationResponse.getToken()));
        return ResponseEntity.ok("a");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserInfo(@RequestParam Long idUser) {
        UserDto userInfo = userService.getUserInfo(idUser);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUserInfo(@RequestParam Long idUser, @RequestBody UserDto updatedUserDto) {
        UserDto updatedUserInfo = userService.updateUserInfo(idUser, updatedUserDto);
        return ResponseEntity.ok(updatedUserInfo);
    }
    @GetMapping("/readToken")
    public String readCookie(@CookieValue(value = "sessionToken") String token) {
        return "Hey, token is: " + token;
    }
}