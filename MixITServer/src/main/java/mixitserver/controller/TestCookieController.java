package mixitserver.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/public/testCookie")
public class TestCookieController {
    @GetMapping("/set")
    public ResponseEntity<String> testCookie(HttpServletResponse response){

       //przydzielanie tokenu

        response.addCookie(new Cookie("sessionToken", "token")); //ustawiam cookie token
        return ResponseEntity.ok("Cookie is working");
    }
    @GetMapping("/read")
    public String readCookie(@CookieValue(value = "sessionToken") String username) {
        return "Hey! My username is " + username;
    }

}
