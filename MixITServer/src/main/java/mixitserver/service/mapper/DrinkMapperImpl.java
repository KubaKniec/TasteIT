package mixitserver.service.mapper;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.model.domain.Ingredient;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.model.dto.IngredientDTO;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DrinkMapperImpl{
    private final IngredientMapperImpl ingredientMapper;

    public DrinkDTO mapToDto(Drink drink) {
        if (drink == null) {
            return null;
        }
        DrinkDTO.DrinkDTOBuilder drinkDTO = DrinkDTO.builder();

        drinkDTO.idDrink(drink.getIdDrink());
        drinkDTO.apiId(drink.getApiId());
        drinkDTO.name(drink.getName());
        // Mapowanie składników na DTO (jeśli istnieją)
        List<IngredientDTO> ingredientDTOs = drink.getIngredients().stream()
                .map(ingredientMapper::mapToDto)
                .toList();
        drinkDTO.ingredients(ingredientDTOs);
        drinkDTO.instructions(drink.getInstructions());
        drinkDTO.isAlcoholic(drink.isAlcoholic());
        drinkDTO.glassType(drink.getGlassType());
        drinkDTO.image(drink.getImage());
        drinkDTO.category(drink.getCategory());

        return drinkDTO.build();
    }

    public Drink mapToDomain(DrinkDTO drinkDTO) {
        if (drinkDTO == null) {
            return null;
        }
        Drink.DrinkBuilder drink = Drink.builder();

        drink.idDrink(drinkDTO.getIdDrink());
        drink.apiId(drinkDTO.getApiId());
        drink.name(drinkDTO.getName());
        // Mapowanie składników na encję (jeśli istnieją)
        List<Ingredient> ingredients = drinkDTO.getIngredients().stream()
                .map(ingredientMapper::mapToDomain)
                .toList();
        drink.ingredients(ingredients);
        drink.instructions(drinkDTO.getInstructions());
        drink.isAlcoholic(drinkDTO.isAlcoholic());
        drink.glassType(drinkDTO.getGlassType());
        drink.image(drinkDTO.getImage());
        drink.category(drinkDTO.getCategory());

        return drink.build();
    }
}
