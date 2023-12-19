package mixitserver.service.mapper;

import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Ingredient;
import mixitserver.model.dto.IngredientDTO;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientMapperImpl {

    public IngredientDTO mapToDto(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }
        return IngredientDTO.builder()
                .idIngredient(ingredient.getIdIngredient())
                .name(ingredient.getName())
                .description(ingredient.getDescription())
                .type(ingredient.getType())
                .isAlcohol(ingredient.getIsAlcohol())
                .strenght(ingredient.getStrenght())
                .imageURL(ingredient.getImageURL())
                .drinks(ingredient.getDrinks())
                .build();
    }

    public Ingredient mapToDomain(IngredientDTO ingredientDTO) {
        if (ingredientDTO == null) {
            return null;
        }
        return Ingredient.builder()
                .idIngredient(ingredientDTO.getIdIngredient())
                .name(ingredientDTO.getName())
                .description(ingredientDTO.getDescription())
                .type(ingredientDTO.getType())
                .isAlcohol(ingredientDTO.getIsAlcohol())
                .strenght(ingredientDTO.getStrenght())
                .imageURL(ingredientDTO.getImageURL())
                .drinks(ingredientDTO.getDrinks())
                .build();
    }
}
