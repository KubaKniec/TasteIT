package mixitserver.service.mapper;

import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.model.domain.Ingredient;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.model.dto.IngredientDTO;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DrinkMapperImpl {

    private final IngredientMapperImpl ingredientMapper;

    public DrinkDTO mapToDto(Drink drink) {
        if (drink == null) {
            return null;
        }
        DrinkDTO.DrinkDTOBuilder drinkDTO = DrinkDTO.builder()
                .idDrink(drink.getIdDrink())
                .apiId(drink.getApiId())
                .name(drink.getName())
                .instructions(drink.getInstructions())
                .isAlcoholic(drink.isAlcoholic())
                .glassType(drink.getGlassType())
                .image(drink.getImage())
                .category(drink.getCategory())
                .popularity(drink.getPopularity());

        List<IngredientDTO> combinedIngredientDTOs = IntStream.range(0, drink.getIngredients().size())
                .mapToObj(i -> {
                    IngredientDTO tempIngredient = ingredientMapper.mapToDto(drink.getIngredients().get(i));
                    tempIngredient.setAmount(drink.getAmounts().get(i));
                    tempIngredient.setDrinks(null);
                    return tempIngredient;
                })
                .toList();

        drinkDTO.ingredients(combinedIngredientDTOs);

        return drinkDTO.build();
    }

    public Drink mapToDomain(DrinkDTO drinkDTO) {
        if (drinkDTO == null) {
            return null;
        }
        Drink.DrinkBuilder drink = Drink.builder()
                .idDrink(drinkDTO.getIdDrink())
                .apiId(drinkDTO.getApiId())
                .name(drinkDTO.getName())
                .instructions(drinkDTO.getInstructions())
                .isAlcoholic(drinkDTO.isAlcoholic())
                .glassType(drinkDTO.getGlassType())
                .image(drinkDTO.getImage())
                .category(drinkDTO.getCategory())
                .popularity(drinkDTO.getPopularity());

        List<Ingredient> ingredients = drinkDTO.getIngredients().stream()
                .map(ingredientMapper::mapToDomain)
                .toList();
        drink.ingredients(ingredients);

        return drink.build();
    }
}
