package pl.jakubkonkol.tasteitserver.apitools;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.jakubkonkol.tasteitserver.exception.ApiRequestException;
import pl.jakubkonkol.tasteitserver.factory.IngredientMealFactory;
import pl.jakubkonkol.tasteitserver.factory.PostMealFactory;
import pl.jakubkonkol.tasteitserver.model.Ingredient;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.service.IngredientService;
import pl.jakubkonkol.tasteitserver.service.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Jakub Konkol
 * Fetcher for food data from theMealDB API
 * Probably needs more testing and error handling
 */
@Component
@RequiredArgsConstructor
public class FoodFetcher {
    private static final Logger LOGGER = Logger.getLogger(FoodFetcher.class.getName());

    private final IngredientService ingredientService;
    private final PostService postService;
    private final OkHttpClient client;
    private final IngredientMealFactory ingredientFactory;
    private final PostMealFactory postFactory;
    private final String foodFinderURL = "https://themealdb.com/api/json/v1/1/search.php?f=";
    private final String ingredientListURL = "https://www.themealdb.com/api/json/v1/1/list.php?i=list";
    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Wipes ingredient and post collection and then populates it with food data
     */
    public void populateDBWithFood() {
        var ingredients = fetchIngredients();
        var foodPosts = searchFoodForEveryLetter();

        ingredientService.saveAll(ingredients);
        postService.saveAll(foodPosts);
    }

    /**
     * Api call for food data for every letter of the alphabet
     * @return List of food data
     */
    public List<Post> searchFoodForEveryLetter() {
        List<Future<List<Post>>> futures = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            var url = foodFinderURL + c;
            futures.add(executor.submit(() -> fetchFood(url)));
        }

        List<Post> foodList = new ArrayList<>();
        for (var future : futures) {
            try {
                foodList.addAll(future.get());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error fetching food for a letter", e);
            }
        }
        return foodList;
    }

    /**
     * Api call for ingredient data
     * @return List of ingredients
     */
    public List<Ingredient> fetchIngredients() {
        var req = new Request.Builder().url(ingredientListURL).build();
        try (var response = client.newCall(req).execute()) {
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
        var req = new Request.Builder().url(url).build();
        try (var response = client.newCall(req).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiRequestException("Request failed with HTTP error code: " + response.code());
            }
            var rawData = response.body().string();
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
        var foodData = new JSONObject(rawData);
        List<Post> foodList = new ArrayList<>();
        if (!foodData.has("meals") || foodData.isNull("meals")) {
            return foodList;
        }

        var food = foodData.getJSONArray("meals");
        food.forEach(foodItem -> {
            var foodObj = (JSONObject) foodItem;
            var newPost = postFactory.createPost(foodObj);
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
        var ingredientData = new JSONObject(rawData);
        List<Ingredient> ingredientsList = new ArrayList<>();
        if (!ingredientData.has("meals") || ingredientData.isNull("meals")) {
            return ingredientsList;
        }

        var ingredients = ingredientData.getJSONArray("meals");
        ingredients.forEach(ingredient -> {
            var ingredientObj = (JSONObject) ingredient;
            var newIngredient = ingredientFactory.createIngredient(ingredientObj);
            ingredientsList.add(newIngredient);
        });

        return ingredientsList;
    }
}
