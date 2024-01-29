package mixitserver.service.mapper;

import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.model.domain.Ingredient;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.model.dto.IngredientDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class IngredientMapperImpl {

/*    private final DrinkMapperImpl drinkMapper;*/

    public IngredientDTO mapToDto(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        IngredientDTO.IngredientDTOBuilder ingredientDTO = IngredientDTO.builder()
                .idIngredient(ingredient.getIdIngredient())
                .name(ingredient.getName())
                .description(ingredient.getDescription())
                .type(ingredient.getType())
                .isAlcohol(ingredient.getIsAlcohol())
                .strenght(ingredient.getStrenght())
                .imageURL(ingredient.getImageURL());

/*        List<DrinkDTO> combinedDrinkDTOs = IntStream.range(0, ingredient.getDrinks().size())
                .mapToObj(i -> {
                    DrinkDTO tempDrink = drinkMapper.mapToDto(ingredient.getDrinks().get(i));
                    tempDrink.setIngredients(null);
                    return tempDrink;
                })
                .toList();

        ingredientDTO.drinks(combinedDrinkDTOs);*/
        //TODO

        return ingredientDTO.build();
    }

    public Ingredient mapToDomain(IngredientDTO ingredientDTO) {
        if (ingredientDTO == null) {
            return null;
        }
        Ingredient.IngredientBuilder ingredient = Ingredient.builder()
                .idIngredient(ingredientDTO.getIdIngredient())
                .name(ingredientDTO.getName())
                .description(ingredientDTO.getDescription())
                .type(ingredientDTO.getType())
                .isAlcohol(ingredientDTO.getIsAlcohol())
                .strenght(ingredientDTO.getStrenght())
                .imageURL(ingredientDTO.getImageURL());

/*        List<Drink> drinks = ingredientDTO.getDrinks().stream()
                .map(drinkMapper::mapToDomain)
                .toList();
        ingredient.drinks(drinks);*/

        return ingredient.build();
    }
}
