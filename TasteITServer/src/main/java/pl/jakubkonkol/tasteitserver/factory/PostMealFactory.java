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
public class PostMealFactory {
    @Autowired
    private IngredientService ingredientService;
    public Post createPost(JSONObject foodObj) {
        Post newPost = new Post();

        // PostMedia
        PostMedia newMedia = new PostMedia();
        List<String> pics = new ArrayList<>();
        newMedia.setTitle(foodObj.getString("strMeal"));
        pics.add(foodObj.getString("strMealThumb"));
        newMedia.setPictures(pics);
        newMedia.setDescription("Very nice meal, drop like I will give u head");
        newPost.setPostMedia(newMedia);

        // Recipe
        Recipe newRecipe = new Recipe();
        String strInstructions = foodObj.getString("strInstructions");
        String[] splittedInstructionsArray = strInstructions.split("\\.");
        Map<Integer, String> splittedInstructionsMap = IntStream.range(0, splittedInstructionsArray.length)
                .boxed()
                .collect(Collectors.toMap(
                        i -> i,
                        i -> splittedInstructionsArray[i]
                ));
        newRecipe.setSteps(splittedInstructionsMap);
        newRecipe.setPictures(null);


        // Too much voodoo
        List<IngredientWrapper> ingredients = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            var ingWrapper = new IngredientWrapper();
            String ingredientName = foodObj.optString("strIngredient" + i, "").toLowerCase();
            String ingredientAmount = foodObj.optString("strMeasure" + i, "").trim();

            if (ingredientName.isBlank() || ingredientAmount.isBlank()) break;
            var measurement = new Measurement();
            measurement.setValue(ingredientAmount);
            measurement.setUnit("unit");
            ingWrapper.setMeasurement(measurement);
            if(this.ingredientService.findByName(ingredientName).isPresent()) {
                System.out.println("Match found: " + ingredientName);
                var ingredient = this.ingredientService.findByName(ingredientName).get();
                ingWrapper.setIngredient(ingredient);
            }else{
                ingWrapper.setIngredient(new Ingredient());
            }
            ingredients.add(ingWrapper);
        }

        newRecipe.setIngredientsMeasurements(ingredients);
        newPost.setRecipe(newRecipe);
        newPost.setPostType(PostType.FOOD);
//        TODO: set proper user and tags, likes, comments etc
        newPost.setUserId("213742069");
        newPost.setTags(null);
        newPost.setLikes(null);
        newPost.setComments(null);

        return newPost;
    }
}
