package pl.jakubkonkol.tasteitserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubkonkol.tasteitserver.dto.PageDto;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.dto.UserProfileDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Ingredient;
import pl.jakubkonkol.tasteitserver.model.Recipe;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.service.PostService;
import pl.jakubkonkol.tasteitserver.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<User> getPost(@PathVariable String userId, @RequestBody UserProfileDto userProfileDto) {

        var user = userService.updateUserProfile(userId, userProfileDto.getDisplayName(),
                userProfileDto.getBio(), userProfileDto.getProfilePicture(),
                userProfileDto.getBirthDate());

        return ResponseEntity.ok(user);
    }

}
