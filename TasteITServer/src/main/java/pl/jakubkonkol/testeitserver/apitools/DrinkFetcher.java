package pl.jakubkonkol.testeitserver.apitools;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.jakubkonkol.testeitserver.exception.ApiRequestException;
import pl.jakubkonkol.testeitserver.factory.IngredientDrinkFactory;
import pl.jakubkonkol.testeitserver.factory.PostDrinkFactory;
import pl.jakubkonkol.testeitserver.model.Ingredient;
import pl.jakubkonkol.testeitserver.model.Post;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import pl.jakubkonkol.testeitserver.service.IngredientService;
import pl.jakubkonkol.testeitserver.service.PostService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class DrinkFetcher {
    private static final Logger LOGGER = Logger.getLogger(DrinkFetcher.class.getName());
    private final String drinkFinderURL = "https://thecocktaildb.com/api/json/v1/1/search.php?f=";
    private final String ingredientFinderURL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?i=";
    private final String ingredientListURL = "https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list";
    private final PostService postService;
    private final IngredientService ingredientService;
    private final IngredientDrinkFactory ingredientFactory;
    private final PostDrinkFactory postFactory;
    private final OkHttpClient client;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void populateDBWithDrinks() throws InterruptedException {
        var ingredients = fetchIngredients();
        ingredientService.saveAll(ingredients);
        Thread.sleep(5000);
        var drinks = searchDrinkForEveryLetter();
//        postService.saveAll(drinks);
    }

    private List<Ingredient> fetchIngredients(){
        var req = new Request.Builder().url(ingredientListURL).build();
        try (var res = client.newCall(req).execute()){
            if (!res.isSuccessful()) {
                throw new ApiRequestException("Request failed with HTTP error code: " + res.code());
            }
            return parseIngredients(res.body().string());
        }catch (IOException e){
            throw new ApiRequestException("Failed to fetch ingredient data: " + e.getMessage());
        }
    }
    private List<Ingredient> parseIngredients(String rawData){
        var ingredientData = new JSONObject(rawData);
        List<Ingredient> ingredientsList = new ArrayList<>();
        if (!ingredientData.has("drinks") || ingredientData.isNull("drinks")) {
            return ingredientsList;
        }
        var ingredients = ingredientData.getJSONArray("drinks");
        ingredients.forEach(ingredient -> {
            var ingredientObj = (JSONObject) ingredient;
            var ingredientName = ingredientObj.getString("strIngredient1");
            var newIngredient = fetchIngredientByName(ingredientName);
            ingredientsList.add(newIngredient);
        });

        return ingredientsList;
    }

    public List<Post> searchDrinkForEveryLetter() throws InterruptedException {
        List<Future<List<Post>>> futures = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            Thread.sleep(100);
            String url = drinkFinderURL + c;
            futures.add(executor.submit(() -> fetchDrinks(url)));
        }
        List<Post> drinkList = new ArrayList<>();
        for (var future: futures) {
            try {
                Thread.sleep(100);
                drinkList.addAll(future.get());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error occurred while fetching drinks", e);
            }
        }
        return drinkList;
    }

    public List<Post> fetchDrinks(String url) {
        Request req = new Request.Builder().url(url).build();
        try (Response res = client.newCall(req).execute()) {
            if (res.code() == 429) { // Rate limit exceeded
                System.out.println(url);
                LOGGER.log(Level.WARNING, "Rate limit exceeded. Waiting before retrying...");
                Thread.sleep(200);
                return fetchDrinks(url);
            }
            if (!res.isSuccessful()) {
                throw new ApiRequestException("Request failed with HTTP error code: " + res.code());
            }
            String rawData = res.body().string();
            if (StringUtils.isBlank(rawData)) {
                return new ArrayList<>();
            }
            return parseDrinks(rawData);
        } catch (IOException e) {
            throw new ApiRequestException("Failed to fetch drink data: " + e.getMessage());
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Post> parseDrinks(String rawData) {
        JSONObject drinkData = new JSONObject(rawData);
        List<Post> drinkList = new ArrayList<>();
        if (!drinkData.has("drinks") || drinkData.isNull("drinks")) {
            return drinkList;
        }
        drinkData.getJSONArray("drinks").forEach(drinkItem -> {
            JSONObject drinkObj = (JSONObject) drinkItem;
            Post newPost = postFactory.createPost(drinkObj);
//            drinkList.add(newPost);
        });
        return drinkList;
    }

    public Ingredient fetchIngredientByName(String name) {
        System.out.println("Fetching ingredient: " + name);
        if(Objects.equals(name, "Creme de Cassis")){
            return new Ingredient();
        }
        var url = (ingredientFinderURL + name).replace(" ", "%20");
        System.out.println(url);
        Request req = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(req).execute()) {
            if (response.code() == 429) { // Rate limit exceeded
                LOGGER.log(Level.WARNING, "Rate limit exceeded. Waiting before retrying...");
                Thread.sleep(1000);
                return fetchIngredientByName(name);
            }
            if (!response.isSuccessful()) {
                throw new RemoteException("Request failed with HTTP error code: " + response.code());
            }
            String rawData = response.body().string();
            //jest git
            return parseIngredient(rawData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch ingredient data", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Ingredient parseIngredient(String rawData) {
        JSONObject ingredientData = new JSONObject(rawData);
        if (!ingredientData.has("ingredients") || ingredientData.isNull("ingredients")) {
            return null;
        }
        JSONObject ingredientObj = ingredientData.getJSONArray("ingredients").getJSONObject(0);
        return ingredientFactory.createIngredient(ingredientObj);
    }
}