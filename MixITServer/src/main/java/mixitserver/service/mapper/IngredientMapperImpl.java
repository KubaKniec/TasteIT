package mixitserver.service.mapper;

import mixitserver.model.domain.Ingredient;
import mixitserver.model.dto.IngredientDTO;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapperImpl implements IngredientMapper{

    @Override
    public IngredientDTO mapToDto(Ingredient ingredient) {
        if (ingredient == null){
            return null;
        }
        IngredientDTO.IngredientDTOBuilder ingredientDTO = IngredientDTO.builder();

        ingredientDTO.idDrink(ingredient.getIdIngredient());
        ingredientDTO.name(ingredient.getName());
        ingredientDTO.amount(ingredient.getAmount());
//        ingredientDTO.drink(ingredient.getDrink()); //TODO Check if its ok

        return ingredientDTO.build();
    }

    @Override
    public Ingredient mapToDomain(IngredientDTO ingredientDTO) {
        if (ingredientDTO == null) {
            return null;
        }
        Ingredient.IngredientBuilder ingredient = Ingredient.builder();

        ingredient.idIngredient(ingredientDTO.getIdDrink());
        ingredient.name(ingredientDTO.getName());
        ingredient.amount(ingredientDTO.getAmount());
//        ingredient.drink(ingredientDTO.getDrink());

        return ingredient.build();
    }
}
