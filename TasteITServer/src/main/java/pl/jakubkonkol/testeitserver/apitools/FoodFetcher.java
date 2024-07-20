package pl.jakubkonkol.testeitserver.apitools;

import io.micrometer.common.util.StringUtils;
import pl.jakubkonkol.testeitserver.exception.ApiRequestException;
import pl.jakubkonkol.testeitserver.factory.IngredientFactory;
import pl.jakubkonkol.testeitserver.factory.PostFactory;
import pl.jakubkonkol.testeitserver.model.*;
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



/**
 * Author: Jakub Konkol
 * Fetcher for food data from theMealDB API
 * Probably needs more testing and error handling
 */
@Component
@RequiredArgsConstructor
public class FoodFetcher {
    private final IngredientService ingredientService;
    private final PostService postService;
    private final OkHttpClient client;
    private final IngredientFactory ingredientFactory;
    private final PostFactory postFactory;
    private final String foodFinderURL = "https://themealdb.com/api/json/v1/1/search.php?f=";
    private final String ingredientListURL = "https://www.themealdb.com/api/json/v1/1/list.php?i=list";

    /**
     * Wipes ingredient and post collection and then populates it with food data
     */
    public void populateDBWithFood(){
        ingredientService.deleteAll();
        postService.deleteAll();

        ingredientService.saveAll(fetchIngredients());
        postService.saveAll(searchFoodForEveryLetter());
    }

    /**
     * Api call for food data for every letter of the alphabet
     * @return List of food data
     *
     */
    public List<Post> searchFoodForEveryLetter() {
        List<Post> foodList = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            String url = foodFinderURL + c;
            foodList.addAll(fetchFood(url));
        }
        return foodList;
    }

    /**
     * Api call for ingredient data
     * @return List of ingredients
     */
    public List<Ingredient> fetchIngredients() {
        Request req = new Request.Builder().url(ingredientListURL).build();
        try (Response response = client.newCall(req).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiRequestException("Request failed with HTTP error code: " + response.code());
            }
            return parseIngredients(response.body().string());
        } catch (IOException e) {
            throw new ApiRequestException("Failed to fetch ingredients: " + e.getMessage());
        }
    }

    /**
     * Api call for food data
     * @param url URL for the API call
     * @return List of food data
     */
    public List<Post> fetchFood(String url) {
        Request req = new Request.Builder().url(url).build();
        try (Response response = client.newCall(req).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiRequestException("Request failed with HTTP error code: " + response.code());
            }
            String rawData = response.body().string();
            if (StringUtils.isBlank(rawData)) {
                return new ArrayList<>();
            }
            return parseFood(rawData);
        } catch (IOException e) {
            throw new ApiRequestException("Failed to fetch food: " + e.getMessage());
        }
    }

    /**
     * Parses food data from the API
     * @param rawData Raw data from the API
     * @return List of food data
     */
    private List<Post> parseFood(String rawData) {
        JSONObject foodData = new JSONObject(rawData);
        List<Post> foodList = new ArrayList<>();
        if (!foodData.has("meals") || foodData.isNull("meals")) {
            return foodList;
        }

        JSONArray food = foodData.getJSONArray("meals");
        food.forEach(foodItem -> {
            JSONObject foodObj = (JSONObject) foodItem;
            Post newPost = postFactory.createPost(foodObj);
            foodList.add(newPost);
        });

        return foodList;
    }

    /**
     * Parses ingredient data from the API
     * @param rawData Raw data from the API
     * @return List of ingredients
     */
    private List<Ingredient> parseIngredients(String rawData) {
        JSONObject ingredientData = new JSONObject(rawData);
        List<Ingredient> ingredientsList = new ArrayList<>();
        if (!ingredientData.has("meals") || ingredientData.isNull("meals")) {
            return ingredientsList;
        }

        JSONArray ingredients = ingredientData.getJSONArray("meals");
        ingredients.forEach(ingredient -> {
            JSONObject ingredientObj = (JSONObject) ingredient;
            Ingredient newIngredient = ingredientFactory.createIngredient(ingredientObj);
            ingredientsList.add(newIngredient);
        });

        return ingredientsList;
    }
}
