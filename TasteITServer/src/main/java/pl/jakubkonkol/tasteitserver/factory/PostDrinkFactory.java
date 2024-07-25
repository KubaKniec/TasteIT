package pl.jakubkonkol.tasteitserver.factory;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jakubkonkol.tasteitserver.model.*;
import pl.jakubkonkol.tasteitserver.model.enums.PostType;
import pl.jakubkonkol.tasteitserver.service.IngredientService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class PostDrinkFactory {
    @Autowired
    private IngredientService ingredientService;

    public Post createPost(JSONObject postObj) {
        var newPost = new Post();
        var postMedia = new PostMedia();
        var recipe = new Recipe();

        newPost.setPostType(PostType.DRINK);
        postMedia.setTitle(postObj.getString("strDrink"));
        // TODO: Update description as needed
        postMedia.setDescription("Very nice drink, drop like I will give u head");
        postMedia.setPictures(List.of(postObj.getString("strDrinkThumb")));

        String strInstructions = postObj.getString("strInstructions");
        String[] splittedInstructionsArray = strInstructions.split("\\.");
        Map<Integer, String> splittedInstructionsMap = IntStream.range(0, splittedInstructionsArray.length)
                .boxed()
                .collect(Collectors.toMap(
                        j -> j + 1,
                        j -> splittedInstructionsArray[j].trim()
                ));
        recipe.setSteps(splittedInstructionsMap);
        recipe.setPictures(null);

        List<IngredientWrapper> ingredients = new ArrayList<>();
        for (int k = 1; k <= 15; k++) {
            var ingWrapper = new IngredientWrapper();
            String ingredientName = postObj.optString("strIngredient" + k, "");
            String ingredientAmount = postObj.optString("strMeasure" + k, "").trim();

            if (ingredientName.isBlank() || ingredientAmount.isBlank()) break;

            var measure = new Measurement();
            measure.setValue(ingredientAmount);
            measure.setUnit("unit");
            ingWrapper.setMeasurement(measure);
            if(this.ingredientService.findByName(ingredientName).isPresent()) {
                var ingredient = this.ingredientService.findByName(ingredientName).get();
                ingWrapper.setIngredient(ingredient);
            }else{
                ingWrapper.setIngredient(new Ingredient());
            }
            ingredients.add(ingWrapper);
        }

        recipe.setIngredientsMeasurements(ingredients);
        newPost.setPostMedia(postMedia);
        newPost.setRecipe(recipe);

        // TODO: Set proper user and tags, likes, comments, etc.
        newPost.setUserId("69696969");
        newPost.setTags(null);
        newPost.setLikes(null);
        newPost.setComments(null);
        return newPost;
    }


}
