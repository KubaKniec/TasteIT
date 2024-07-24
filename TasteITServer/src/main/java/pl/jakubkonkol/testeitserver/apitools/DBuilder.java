package pl.jakubkonkol.testeitserver.apitools;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import pl.jakubkonkol.testeitserver.service.IngredientService;
import pl.jakubkonkol.testeitserver.service.PostService;

@Component
@RequiredArgsConstructor
public class DBuilder {
    private final FoodFetcher foodFetcher;
    private final DrinkFetcher drinkFetcher;
    private final IngredientService ingredientService;
    private final PostService postService;
    @PostConstruct
    public void buildDataBase() {
        ingredientService.deleteAll();
        postService.deleteAll();
        System.out.println("Food fetcher started");
        foodFetcher.populateDBWithFood();
//        System.out.println("Drink fetcher started");
//        drinkFetcher.populateDBWithDrinks();

        System.out.println("Database built");
    }
}
