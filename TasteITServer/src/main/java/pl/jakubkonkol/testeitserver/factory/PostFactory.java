package pl.jakubkonkol.testeitserver.factory;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.jakubkonkol.testeitserver.model.*;
import pl.jakubkonkol.testeitserver.model.enums.PostType;
import pl.jakubkonkol.testeitserver.service.IngredientService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class PostFactory {
    private final IngredientService ingredientService;
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
            IngredientWrapper ingWrapper = new IngredientWrapper();
            String ingredientName = foodObj.optString("strIngredient" + i, "");
            String measurement = foodObj.optString("strMeasure" + i, "").trim();
            if (ingredientName.isBlank() || measurement.isBlank()) break;

            Optional<Ingredient> optionalIngredient = ingredientService.findByName(ingredientName);
            if (optionalIngredient.isEmpty()) break;

            Ingredient ingredient = optionalIngredient.get();
            ingWrapper.setIngredient(ingredient);
            Measurement newMeasurement = new Measurement();
            newMeasurement.setValue(measurement);
            newMeasurement.setUnit("unit");
            ingWrapper.setMeasurement(newMeasurement);
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
