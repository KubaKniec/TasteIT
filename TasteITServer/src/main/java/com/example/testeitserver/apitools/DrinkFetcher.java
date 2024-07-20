package com.example.testeitserver.apitools;

import com.example.testeitserver.model.Ingredient;
import com.example.testeitserver.model.Post;
import com.example.testeitserver.model.PostMedia;
import com.example.testeitserver.model.Recipe;
import com.example.testeitserver.model.enums.PostType;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrinkFetcher {
    private final String DrinkFinderURL = "www.thecocktaildb.com/api/json/v1/1/search.php?f=";
    private final String IngredientFinderURL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?i=";
    private final String LoremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Phasellus justo est, dignissim et pellentesque at, pretium id ante. Integer odio elit, vestibulum vitae lectus non, " +
            "iaculis euismod sem. Suspendisse gravida odio id pulvinar pulvinar. Pellentesque habitant morbi tristique senectus et netus et " +
            "malesuada fames ac turpis egestas";
    @Getter
    private final List<Recipe> drinks = new ArrayList<>();
    @Getter
    private final List<Ingredient> ingredients = new ArrayList<>();

    public Recipe fetchDrinksByLetters(Character letter) {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(DrinkFinderURL + letter)
                .build();

        try{
            Response response = client.newCall(req).execute();

            if(!response.isSuccessful()) {
                throw new IOException("Request failed with HTTP error code: " + response.code());
            }

            ResponseBody body = response.body();
            String rawData = body.string();
            return parseDrink(rawData);

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch drink data", e);
        }
    }

    private Recipe parseDrink(String rawData) {
        JSONObject drinkData = new JSONObject(rawData);

        if (!drinkData.has("drinks") || drinkData.isNull("drinks")) {
            return null;
        }

        JSONArray drinksArray = drinkData.getJSONArray("drinks");
        List<Post> postsToSave = new ArrayList<>();

        for (int i = 0; i < drinksArray.length(); i++) {
            JSONObject drinkObject = drinksArray.getJSONObject(i);
            Post post = new Post();
            PostMedia postMedia = new PostMedia();
            Recipe recipe = new Recipe();

            post.setPostType(PostType.DRINK);

            postMedia.setTitle(drinkObject.getString("strDrink"));
            postMedia.setDescription(LoremIpsum);
            postMedia.setPictures(List.of(drinkObject.getString("strDrinkThumb")));

            String strInstructions = drinkObject.getString("strInstructions");
            String[] splittedInstructions = strInstructions.split("\\.");
            Map<Integer, String> splittedInstructionsMap = IntStream.range(0, splittedInstructions.length)
                    .boxed()
                    .collect(Collectors.toMap(
                            j -> j + 1,
                            j -> splittedInstructions[j].trim()
                    ));
            recipe.setSteps(splittedInstructionsMap);

            for(int k = 1; k <= 15; k++) {
                String ingredientName = drinkObject.optString("strIngredient" + i);
                String ingredientAmount = drinkObject.optString("strMeasure" + i).trim();


            }

            post.setPostMedia(postMedia);
            post.setRecipe(recipe);

            postsToSave.add(post);
        }

        return null;
    }

    public Ingredient fetchIngredientByName(String name) {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(IngredientFinderURL + name)
                .build();
        try {
            Response response = client.newCall(req).execute();
            if(!response.isSuccessful()) {
                throw new RemoteException("Request failed with HTTP error code: " + response.code());
            }
            ResponseBody body = response.body();
            String rawData = body.string();
            return parseIngredient(rawData);

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch ingredient data", e);
        }
    }

    public Ingredient parseIngredient(String rawData){
        JSONObject ingredientData = new JSONObject(rawData);
        if (ingredientData.has("ingredients") && ingredientData.get("ingredients") instanceof JSONArray) {
            JSONArray ingredientArray = ingredientData.getJSONArray("ingredients");
            JSONObject ingredientObject = ingredientArray.getJSONObject(0);
            Ingredient ingredient = new Ingredient();

            ingredient.setName(ingredientObject.getString("strIngredient"));
            ingredient.setDescription(ingredientObject.optString("strDescription"));
            ingredient.setType(ingredientObject.optString("strType"));
            String strAlcohol = ingredientObject.getString("strAlcohol");
            boolean isAlcohol = strAlcohol.equals("Yes");
            ingredient.setAlcohol(isAlcohol);
            ingredient.setStrength(ingredientObject.optString("strABV"));

            return ingredient;
        }
        else{
            return new Ingredient();
        }
    }
}
