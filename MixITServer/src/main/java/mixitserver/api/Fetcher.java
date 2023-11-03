package mixitserver.api;

import mixitserver.model.domain.Drink;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static mixitserver.api.KnownIdsData.KNOWN_DRIKNS_IDS;

@Component
public class Fetcher {
    private final List<Drink> drinks = new ArrayList<>();
    public static String URL = "www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";
    private String rawData;

    void loadDataById(int id){
        String rawUrl = URL+id;

        try {
            URL url = new URL(rawUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            if(status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                this.rawData = response.toString();
            } else {
                System.out.println("Error, response status: " + status);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error " + e);
        }
    }

//    public List<Drink> parseData() {
//    }

    public void parseDrink(JSONObject drinkJsonObject, List<Drink> drinks) {
        String idDrink = drinkJsonObject.getString("idDrink");
        String name = drinkJsonObject.getString("strDrink");
        String category = drinkJsonObject.getString("strCategory");
        String glassType = drinkJsonObject.getString("strGlass");
        String alcoholic = drinkJsonObject.getString("strAlcoholic");
        String instructions = drinkJsonObject.getString("strInstructions");
        String image = drinkJsonObject.getString("strDrinkThumb");

        Drink drink = new Drink();
//        drink.setAlcoholic(alcoholic.equals("Alcoholic"));
    }
    void fetchAll() {
        for(int id: KNOWN_DRIKNS_IDS) {
//            drinks.add(fetchDrinkById(id));
        }
    }
}
