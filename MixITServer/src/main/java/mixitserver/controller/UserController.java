package mixitserver.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mixitserver.jwt.JwtTokenProvider;
import mixitserver.model.additional.AuthenticationRequest;
import mixitserver.model.additional.AuthenticationResponse;
import mixitserver.model.dto.BarDto;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.model.dto.IngredientDTO;
import mixitserver.model.dto.UserDto;
import mixitserver.service.AuthenticationService;
import mixitserver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserInfo(@CookieValue(value = "sessionToken") String token) {
        UserDto userInfo = userService.getUserInfo(token);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUserInfo(@RequestParam Long idUser, @RequestBody UserDto updatedUserDto) {
        UserDto updatedUserInfo = userService.updateUserInfo(idUser, updatedUserDto);
        return ResponseEntity.ok(updatedUserInfo);
    }

    @GetMapping("/readToken")
    public String readCookie(@CookieValue(value = "sessionToken") String token) {
        System.out.println("\t: "+token);
        String username = jwtTokenProvider.extractUsername(token);
        return "Hey, token is: " + username;
    }

    @PostMapping("/favourites/{drinkId}")
    public ResponseEntity<List<Integer>> addFavouriteDrink(@CookieValue("sessionToken") String token,
                                                           @PathVariable Integer drinkId) {
        List<Integer> favourites = userService.addFavouriteDrink(token, drinkId);
        return ResponseEntity.ok(favourites);
    }

    @PostMapping("/bar")
    public ResponseEntity<BarDto> createBar(@CookieValue("sessionToken") String token,
                                            @RequestBody BarDto barDto) {
        BarDto newBar = userService.createBar(token, barDto);
        return ResponseEntity.ok(newBar);
    }

    @GetMapping("/bar/{barId}")
    public ResponseEntity<BarDto> getBar(@CookieValue("sessionToken") String token, @PathVariable Integer barId) {
        BarDto barInfo = userService.getBarInfo(token, barId);
        return ResponseEntity.ok(barInfo);
    }

    @DeleteMapping("/bar/{barId}")
    public ResponseEntity<?> deleteBar(@CookieValue("sessionToken") String token, @PathVariable Integer barId) {
        userService.deleteBar(token, barId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bar/{barId}")
    public ResponseEntity<List<DrinkDTO>> addDrinkToBar(@CookieValue("sessionToken") String token, @PathVariable Integer barId,
                                                        @RequestParam Integer drinkId) {
        List<DrinkDTO> barDrinks = userService.addDrinkToBar(token, barId, drinkId);
        return ResponseEntity.ok(barDrinks);
    }

    @PostMapping
    public ResponseEntity<List<IngredientDTO>> addIngredient(@CookieValue("sessionToken") String token,
                                                             @RequestParam Integer ingredientId) {
        List<IngredientDTO> userIngredients = userService.addIngredientToUser(token, ingredientId);
        return ResponseEntity.ok(userIngredients);
    }

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getUserIngredients(@CookieValue("sessionToken") String token) {
        List<IngredientDTO> ingredients = userService.getUserIngredients(token);
        return ResponseEntity.ok(ingredients);
    }

}