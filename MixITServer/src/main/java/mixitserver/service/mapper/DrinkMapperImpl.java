package mixitserver.service.mapper;

import mixitserver.model.domain.Drink;
import mixitserver.model.domain.Ingredient;
import mixitserver.model.dto.DrinkDTO;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DrinkMapperImpl implements DrinkMapper{
    @Override
    public DrinkDTO mapToDto(Drink drink) {
        if (drink == null) {
            return null;
        }
        DrinkDTO.DrinkDTOBuilder drinkDTO = DrinkDTO.builder();

        drinkDTO.idDrink(drink.getIdDrink());
        drinkDTO.apiId(drink.getApiId());
        drinkDTO.name(drink.getName());
//        List<Ingredient> list = drink.getIngredients();
//        if (list != null) {
//            drinkDTO.ingredients(new ArrayList<Ingredient>(list));
//        }
        drinkDTO.instructions(drink.getInstructions());
        drinkDTO.isAlcoholic(drink.isAlcoholic());
        drinkDTO.glassType(drink.getGlassType());
        drinkDTO.image(drink.getImage());
        drinkDTO.category(drink.getCategory());

        return drinkDTO.build();
    }

    @Override
    public Drink mapToDomain(DrinkDTO drinkDTO) {
        if (drinkDTO == null){
            return null;
        }

        Drink.DrinkBuilder drink = Drink.builder();

        drink.idDrink(drinkDTO.getIdDrink());
        drink.apiId(drinkDTO.getApiId());
        drink.name(drinkDTO.getName());
//        List<Ingredient> list = drinkDTO.getIngredients();
//        if (list != null) {
//            drink.ingredients(new ArrayList<Ingredient>(list));
//        }
        drink.instructions(drinkDTO.getInstructions());
        drink.isAlcoholic(drinkDTO.isAlcoholic());
        drink.glassType(drinkDTO.getGlassType());
        drink.image(drinkDTO.getImage());
        drink.category(drinkDTO.getCategory());

        return drink.build();
    }
}
