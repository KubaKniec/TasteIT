package mixitserver.service;

import lombok.RequiredArgsConstructor;
import mixitserver.jwt.JwtTokenProvider;
import mixitserver.model.domain.Bar;
import mixitserver.model.domain.Drink;
import mixitserver.model.domain.Ingredient;
import mixitserver.model.domain.User;
import mixitserver.model.dto.BarDto;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.model.dto.IngredientDTO;
import mixitserver.model.dto.UserDto;
import mixitserver.repository.BarRepository;
import mixitserver.repository.DrinkRepository;
import mixitserver.repository.IngredientRepository;
import mixitserver.repository.UserRepository;
import mixitserver.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final DrinkRepository drinkRepository;
    private final BarRepository barRepository;

    private final IngredientRepository ingredientRepository;

    public UserDto getUserInfo(String token) {
        Long userId = jwtTokenProvider.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + userId));

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());

        userDto.setBars(user.getBars().stream()
                .map(bar -> {
                    BarDto barDto = new BarDto();
                    barDto.setName(bar.getName());
                    return barDto;
                })
                .toList());

        userDto.setFavouriteDrinks(user.getFavouriteDrinks().stream()
                .map(drink -> DrinkDTO.builder()
                        .name(drink.getName())
                        .instructions(drink.getInstructions())
                        .isAlcoholic(drink.isAlcoholic())
                        .glassType(drink.getGlassType())
                        .image(drink.getImage())
                        .category(drink.getCategory())
                        .popularity(drink.getPopularity())
                        .build()).toList());

        return userDto;
    }

    public UserDto updateUserInfo(Long idUser, UserDto updatedUserDto) {
        User existingUser = userRepository.findByIdUser(idUser)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + idUser));

        if (!existingUser.getEmail().equals(updatedUserDto.getEmail()) && userRepository.existsByEmail(updatedUserDto.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        if (!existingUser.getActualUsername().equals(updatedUserDto.getUsername()) && userRepository.existsByUsername(updatedUserDto.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        existingUser.setUsername(updatedUserDto.getUsername());
        existingUser.setEmail(updatedUserDto.getEmail());

        User updatedUser = userRepository.save(existingUser);

        String newToken = jwtTokenProvider.generateToken(updatedUser);

        UserDto responseDto = UserMapper.toDto(updatedUser);
        responseDto.setToken(newToken);

        return responseDto;
    }

    public List<Integer> addFavouriteDrink(String token, Integer drinkId) {
        Long userId = jwtTokenProvider.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + userId));
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new RuntimeException("Drink not found"));

        if (user.getFavouriteDrinks().contains(drink)) {
            throw new RuntimeException("Drink already in favourite list");
        }

        user.getFavouriteDrinks().add(drink);
        userRepository.save(user);

        drink.getUsers().add(user);
        drinkRepository.save(drink);

        return user.getFavouriteDrinks().stream()
                .map(Drink::getIdDrink)
                .toList();
    }

    public BarDto createBar(String token, BarDto barDto) {
        Long userId = jwtTokenProvider.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + userId));

        if (barRepository.existsByNameAndUserIdUser(barDto.getName(), userId)) {
            throw new RuntimeException("Bar with this name already exists");
        }

        Bar bar = new Bar();
        bar.setName(barDto.getName());
        bar.setUser(user);
        Bar newBar = barRepository.save(bar);

        BarDto newBarDto = new BarDto();
        newBarDto.setName(newBar.getName());

        return newBarDto;
    }

    public BarDto getBarInfo(String token, Integer barId) {
        Long userId = jwtTokenProvider.extractUserId(token);
        if(!userRepository.existsById(userId)) {
            throw new RuntimeException("No user found with id: " + userId);
        }

        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar not found"));

        if (!barRepository.existsByIdBarAndUserIdUser(barId, userId)) {
           throw new RuntimeException("Bar with id: " + barId + " does not belong to user with id: " + userId);
        }

        BarDto barDto = new BarDto();
        barDto.setName(bar.getName());

        return barDto;
    }

    public void deleteBar(String token, Integer barId) {
        Long userId = jwtTokenProvider.extractUserId(token);
        if(!userRepository.existsById(userId)) {
            throw new RuntimeException("No user found with id: " + userId);
        }

        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar not found"));

        if (!barRepository.existsByIdBarAndUserIdUser(barId, userId)) {
            throw new RuntimeException("Bar with id: " + barId + " does not belong to user with id: " + userId);
        }

        barRepository.delete(bar);
    }

    public List<DrinkDTO> addDrinkToBar(String token, Integer barId, Integer drinkId) {
        Long userId = jwtTokenProvider.extractUserId(token);

        if(!userRepository.existsById(userId)) {
            throw new RuntimeException("No user found with id: " + userId);
        }

        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new RuntimeException("Bar not found"));

        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new RuntimeException("Drink not found"));

        if (!barRepository.existsByIdBarAndUserIdUser(barId, userId)) {
            throw new RuntimeException("Bar with id: " + barId + " does not belong to user with id: " + userId);
        }

        if (bar.getDrinks().contains(drink)) {
            throw new RuntimeException("Bar with id: " + barId + " already has a drink with id" + drinkId);
        }

        bar.getDrinks().add(drink);
        barRepository.save(bar);

        drink.getBars().add(bar);
        drinkRepository.save(drink);

        return bar.getDrinks().stream().map(barDrink -> DrinkDTO.builder()
                .name(barDrink.getName())
                .instructions(barDrink.getInstructions())
                .isAlcoholic(barDrink.isAlcoholic())
                .glassType(barDrink.getGlassType())
                .image(barDrink.getImage())
                .category(barDrink.getCategory())
                .popularity(barDrink.getPopularity())
                .build()).toList();
    }


    public List<IngredientDTO> addIngredientToUser(String token, Integer ingredientId) {
        Long userId = jwtTokenProvider.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        if (user.getIngredients().contains(ingredient)) {
            throw new RuntimeException("Ingredient already added to user");
        }

        user.getIngredients().add(ingredient);
        userRepository.save(user);

        ingredient.getUsers().add(user);
        ingredientRepository.save(ingredient);

        return user.getIngredients().stream()
                .map(userIngredient -> IngredientDTO.builder()
                        .name(userIngredient.getName())
                        .description(userIngredient.getDescription())
                        .type(userIngredient.getType())
                        .isAlcohol(userIngredient.getIsAlcohol())
                        .strenght(userIngredient.getStrenght())
                        .imageURL(userIngredient.getImageURL())
                        .build())
                .toList();
    }

    public List<IngredientDTO> getUserIngredients(String token) {
        Long userId = jwtTokenProvider.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return  user.getIngredients().stream()
                .map(userIngredient -> IngredientDTO.builder()
                        .name(userIngredient.getName())
                        .description(userIngredient.getDescription())
                        .type(userIngredient.getType())
                        .isAlcohol(userIngredient.getIsAlcohol())
                        .strenght(userIngredient.getStrenght())
                        .imageURL(userIngredient.getImageURL())
                        .build())
                .toList();
    }

}
