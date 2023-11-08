package mixitserver.controller;

import lombok.RequiredArgsConstructor;
import mixitserver.model.additional.Filter;
import mixitserver.model.domain.Drink;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.model.enums.IngredientMatchType;
import mixitserver.service.DrinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/public/drink")
@RequiredArgsConstructor
public class DrinkController {
    private final DrinkService drinkService;
    @PostMapping("/save")
    public ResponseEntity<Drink> save(@RequestBody Drink drink){
        return ResponseEntity.ok(drinkService.save(drink));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(drinkService.getDrinkByIdDrink(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<DrinkDTO>> getAll() {
        return ResponseEntity.ok(drinkService.getAll());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<DrinkDTO>> getTop10DrinksByPopularity() {
        return ResponseEntity.ok(drinkService.getTop10DrinksByPopularity());
    }

    @GetMapping("/search")
    public ResponseEntity<List<DrinkDTO>> getDrinkByName(@RequestParam String query) {
        return ResponseEntity.ok(drinkService.getDrinksByName(query));
    }

    @GetMapping("/daily")
    public ResponseEntity<DrinkDTO> getDailyDrink() {
        return ResponseEntity.ok(drinkService.getDailyDrink());
    }
    @GetMapping("/filter")
    public ResponseEntity<List<DrinkDTO>> filterDrinks(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "alcoholic", required = false) Boolean alcoholic,
            @RequestParam(value = "glassType", required = false) String glassType) {

        Filter filter = new Filter(category, alcoholic, glassType);

        return ResponseEntity.ok(drinkService.filterDrinks(filter));
    }

    @GetMapping("/filter/withIngredients")
    public ResponseEntity<List<DrinkDTO>> getDrinksWithFilters(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "alcoholic", required = false) Boolean alcoholic,
            @RequestParam(value = "glassType", required = false) String glassType,
            @RequestParam(value = "matchType") String matchType,
            @RequestParam(value = "ingredientNames") List<String> ingredientNames,
            @RequestParam(value = "minIngredientCount", required = false) Integer minIngredientCount) {

        Filter filter = new Filter(category, alcoholic, glassType);

        return ResponseEntity.ok(drinkService.getDrinksWithFilters(filter, matchType, ingredientNames, minIngredientCount));
    }
}
