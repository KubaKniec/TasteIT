package mixitserver.api;

import lombok.Getter;
import mixitserver.model.Drink;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fetcherv2 {
    private String URL = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";
    private final List<Integer> tcdbIds = KnownIdsData.KNOWN_DRIKNS_IDS;
    @Getter
    private ArrayList<Drink> drinks = new ArrayList<>();


    public Drink fetchDrinkById(int id) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(URL+id)
                .build();
        try{
            Response response = client.newCall(req).execute();
            String rawData = response.body().string();
            return parseDrink(rawData);
        } catch (IOException e) {
            throw new IOException("Error " + e);
        }
    }
    public Drink parseDrink(String rawData){
        JSONObject drinkData = new JSONObject(rawData);
        if (drinkData.has("drinks") && !drinkData.isNull("drinks")) {
            JSONArray drinksArray = drinkData.getJSONArray("drinks");
            if (!drinksArray.isEmpty()) {
                JSONObject drinkObject = drinksArray.getJSONObject(0);
                Drink newDrink = new Drink();
                newDrink.setApiId(Integer.parseInt(drinkObject.getString("idDrink")));
                newDrink.setName(drinkObject.getString("strDrink"));
                newDrink.setInstructions(drinkObject.getString("strInstructions"));
                newDrink.setAlcoholic("Alcoholic".equalsIgnoreCase(drinkObject.getString("strAlcoholic")));
                newDrink.setGlassType(drinkObject.getString("strGlass"));
                newDrink.setImage(drinkObject.getString("strDrinkThumb"));
                newDrink.setCategory(drinkObject.getString("strCategory"));
                newDrink.setIngredients(null); //!

                return newDrink;
            }
        }
        return null;
    }
    public void fetchAll(){
        for(Integer id : tcdbIds){
            try {
                Drink drink = fetchDrinkById(id);
                if(drink != null){
                    drinks.add(drink);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
