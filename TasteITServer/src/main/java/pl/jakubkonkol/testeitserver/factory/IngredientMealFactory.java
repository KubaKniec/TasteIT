package pl.jakubkonkol.testeitserver.factory;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.jakubkonkol.testeitserver.model.Ingredient;

@Component
@RequiredArgsConstructor
public class IngredientMealFactory {
    public Ingredient createIngredient(JSONObject ingredientObj){
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientObj.getString("strIngredient"));
        ingredient.setDescription(ingredientObj.optString("strDescription", "No description available"));
        ingredient.setType("Food");
        ingredient.setStrength("0");
        ingredient.setAlcohol(false);
        ingredient.setImageURL(null);
        return ingredient;
    }
}
