package mixitserver.api;

import lombok.Getter;
import mixitserver.exception.ApiDataParsingRuntimeException;
import mixitserver.exception.FetchException;
import mixitserver.model.Drink;
import mixitserver.model.Ingredient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fetcherv2 {
    private final String URL = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";
    private final List<Integer> tcdbIds = KnownIdsData.KNOWN_DRIKNS_IDS;
    @Getter
    private final ArrayList<Drink> drinks = new ArrayList<>();


    public Drink fetchDrinkById(int id) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(URL+id)
                .build();
        try{
            Response response = client.newCall(req).execute();

            if(!response.isSuccessful()) {
                throw new IOException("Request failed with HTTP error code: " + response.code());
            }

            ResponseBody body = response.body();
            if(body != null) {
                String rawData = body.string();
                return parseDrink(rawData);
            } else {
                throw new IOException("Response body is null");
            }

        } catch (IOException e) {
            throw new FetchException("Failed to fetch drink data", e);
        }
    }
    public Drink parseDrink(String rawData){
        JSONObject drinkData = new JSONObject(rawData);

        if (!drinkData.has("drinks") || drinkData.isNull("drinks")) {
            return null;
        }

        JSONArray drinksArray = drinkData.getJSONArray("drinks");

        JSONObject drinkObject = drinksArray.getJSONObject(0);
        Drink newDrink = new Drink();
        newDrink.setApiId(Integer.parseInt(drinkObject.getString("idDrink")));
        newDrink.setName(drinkObject.getString("strDrink"));
        newDrink.setInstructions(drinkObject.getString("strInstructions"));
        newDrink.setAlcoholic("Alcoholic".equalsIgnoreCase(drinkObject.getString("strAlcoholic")));
        newDrink.setGlassType(drinkObject.getString("strGlass"));
        newDrink.setImage(drinkObject.getString("strDrinkThumb"));
        newDrink.setCategory(drinkObject.getString("strCategory"));

        List<Ingredient> ingredients = new ArrayList<>();
        for(int i = 1; i <= 15; i++) {
            String ingredientName = drinkObject.optString("strIngredient" + i);
            String ingredientAmount = drinkObject.optString("strMeasure" + i);

            if(ingredientName != null && !ingredientName.isEmpty()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientName);
                ingredient.setAmount(ingredientAmount);
                ingredient.setDrink(newDrink);
                ingredients.add(ingredient);
            } else {
                break;
            }
        }
        newDrink.setIngredients(ingredients);

        return newDrink;
    }

    public void fetchAll(){
        for(Integer id : tcdbIds){
            try {
                Drink drink = fetchDrinkById(id);
                if(drink != null){
                    System.out.println(drink.getName());

                    drinks.add(drink);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
