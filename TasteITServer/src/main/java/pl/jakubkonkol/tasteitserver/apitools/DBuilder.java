package pl.jakubkonkol.tasteitserver.apitools;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import pl.jakubkonkol.tasteitserver.service.IngredientService;
import pl.jakubkonkol.tasteitserver.service.PostService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class DBuilder {
    private final FoodFetcher foodFetcher;
    private final IngredientFetcher ingredientFetcher;
    private final DrinkFetcher drinkFetcher;
    private final IngredientService ingredientService;
    private final PostService postService;
    private static final Logger LOGGER = Logger.getLogger(DBuilder.class.getName());
//    @PostConstruct
    public void buildDataBase() throws IOException {
        ingredientService.deleteAll();
        postService.deleteAll();
        LOGGER.log(Level.INFO, "Database cleared, building new one");
        foodFetcher.populateDBWithFood();
        ingredientFetcher.populateDBWithIngredients();
        drinkFetcher.populateDBWithDrinks();

        LOGGER.log(Level.INFO, "Database built");
    }
}
