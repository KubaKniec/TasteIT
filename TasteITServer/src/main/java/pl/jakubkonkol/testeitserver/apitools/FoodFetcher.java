package pl.jakubkonkol.testeitserver.apitools;

import pl.jakubkonkol.testeitserver.model.*;
import pl.jakubkonkol.testeitserver.model.enums.PostType;
import pl.jakubkonkol.testeitserver.service.IngredientService;
import pl.jakubkonkol.testeitserver.service.PostService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Author: Jakub Konkol
 * Fetcher for food data from theMealDB API
 *
 * Glitchy, needs better error handling
 * Also probably needs to be refactored and needs LOT OF TESTING
 * Also, proper user ID should be used, and likes and comments should be added
 * TODO: Refactor, test, error handling, user ID, likes, comments
 */
@Component
@RequiredArgsConstructor
public class FoodFetcher {
    private final IngredientService ingredientService;
    private final PostService postService;
    private final String foodFinderURL = "https://themealdb.com/api/json/v1/1/search.php?f=";
    private final String ingredientListURL = "https://www.themealdb.com/api/json/v1/1/list.php?i=list";

    public void populateDBWithFood(){
        ingredientService.deleteAll();
        postService.deleteAll();

        ingredientService.saveAll(fetchIngredients());
        postService.saveAll(searchFoodForEveryLetter());
    }

    public List<Post> searchFoodForEveryLetter() {
        List<Post> foodList = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            String url = foodFinderURL + c;
            foodList.addAll(fetchFood(url));
        }
        return foodList;
    }

    public List<Ingredient> fetchIngredients(){
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(ingredientListURL)
                .build();
        try{
            Response response = client.newCall(req).execute();
            if(!response.isSuccessful()) {
                throw new IOException("Request failed with HTTP error code: " + response.code());
            }
            ResponseBody body = response.body();
            String rawData = body.string();
            return parseIngredients(rawData);
        } catch (IOException e) {
            //TODO: ADD CUSTOM EXCEPTION
            throw new RuntimeException(e);
        }
    }

    public List<Post> fetchFood(String url){
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(url)
                .build();
        try{
            Response response = client.newCall(req).execute();
            if(!response.isSuccessful()) {
                throw new IOException("Request failed with HTTP error code: " + response.code());
            }
            ResponseBody body = response.body();
            String rawData = body.string();
            if(rawData.isBlank() || rawData.isEmpty()) return new ArrayList<>();
            return parseFood(rawData);
        } catch (IOException e) {
            //TODO: ADD CUSTOM EXCEPTION
            throw new RuntimeException(e);
        }
    }

    private List<Post> parseFood(String rawData) {
        JSONObject foodData = new JSONObject(rawData);
        List<Post> foodList = new ArrayList<>();
        if (!foodData.has("meals") || foodData.isNull("meals")) {
            return foodList;
        }

        JSONArray food = foodData.getJSONArray("meals");
        food.forEach(foodItem -> {
            JSONObject foodObj = (JSONObject) foodItem;
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
                if (optionalIngredient.isEmpty()) {
                    break;
                }
                Ingredient ingredient = optionalIngredient.get();
                ingWrapper.setIngredient(ingredient);
                Measurement newMeasurement = new Measurement();
                newMeasurement.setValue(measurement);
                newMeasurement.setUnit("unit");
                ingWrapper.setMeasurement(newMeasurement);
                ingredients.add(ingWrapper);
            }

            newRecipe.setIngredientsMeasurements(ingredients);

            // Post
            newPost.setRecipe(newRecipe);
            newPost.setPostType(PostType.FOOD);
            newPost.setUserId("213742069");
            newPost.setTags(null);
            newPost.setLikes(null);
            newPost.setComments(null);

            foodList.add(newPost);
        });

        return foodList;
    }

    private List<Ingredient> parseIngredients(String rawData) {
        JSONObject ingredientData = new JSONObject(rawData);
        List<Ingredient> ingredientsList = new ArrayList<>();
        if (!ingredientData.has("meals") || ingredientData.isNull("meals")) {
            return ingredientsList;
        }

        JSONArray ingredients = ingredientData.getJSONArray("meals");
        ingredients.forEach(ingredient -> {
            JSONObject ingredientObj = (JSONObject) ingredient;
            Ingredient newIngredient = new Ingredient();
            newIngredient.setName(ingredientObj.getString("strIngredient"));
            if (ingredientObj.isNull("strDescription")) {
                newIngredient.setDescription("No description available");
            } else {
                newIngredient.setDescription(ingredientObj.getString("strDescription"));
            }
            newIngredient.setType("Food");
            newIngredient.setStrength("0");
            newIngredient.setAlcohol(false);
            newIngredient.setImageURL(null);
            ingredientsList.add(newIngredient);
        });

        return ingredientsList;
    }
}
