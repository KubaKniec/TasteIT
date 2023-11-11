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
                .amount(ingredient.getAmount())
                .build();
    }

    public Ingredient mapToDomain(IngredientDTO ingredientDTO) {
        if (ingredientDTO == null) {
            return null;
        }
        return Ingredient.builder()
                .idIngredient(ingredientDTO.getIdIngredient())
                .name(ingredientDTO.getName())
                .amount(ingredientDTO.getAmount())
                .build();
    }
}
