package mixitserver.api;

import lombok.Getter;
import mixitserver.exception.FetchException;
import mixitserver.model.domain.Drink;
import mixitserver.model.domain.Ingredient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FetcherV2 {
    private final String DrinkFinderURL = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";
    private final String IngredientFinderURL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?i=";
    private final List<Integer> tcdbIds = KnownIdsData.KNOWN_DRIKNS_IDS;
    @Getter
    private final ArrayList<Drink> drinks = new ArrayList<>();
    @Getter
    private final ArrayList<Ingredient> ingredients = new ArrayList<>();


    public Drink fetchDrinkById(int id) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(DrinkFinderURL+id)
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public Drink parseDrink(String rawData) throws IOException, InterruptedException {
        JSONObject drinkData = new JSONObject(rawData);

        if (!drinkData.has("drinks") || drinkData.isNull("drinks")) {
            return null;
        }

        JSONArray drinksArray = drinkData.getJSONArray("drinks");

        JSONObject drinkObject = drinksArray.getJSONObject(0);
        Drink newDrink = new Drink();
        newDrink.setApiId(Integer.parseInt(drinkObject.getString("idDrink")));
        newDrink.setName(drinkObject.getString("strDrink"));

        String strInstructions = drinkObject.getString("strInstructions");
        String[] splittedInstructionsArray = strInstructions.split("\\.");
        List<String> splittedInstructionsList = Arrays.stream(splittedInstructionsArray)
                .map(String::trim)
                .toList();
        newDrink.setInstructions(splittedInstructionsList);

        newDrink.setAlcoholic("Alcoholic".equalsIgnoreCase(drinkObject.getString("strAlcoholic")));
        newDrink.setGlassType(drinkObject.getString("strGlass"));
        newDrink.setImage(drinkObject.getString("strDrinkThumb"));
        newDrink.setCategory(drinkObject.getString("strCategory"));

        List<Ingredient> newDrinkingredients = new ArrayList<>();
        for(int i = 1; i <= 15; i++) {
            String ingredientName = drinkObject.optString("strIngredient" + i);
            String ingredientAmount = drinkObject.optString("strMeasure" + i).trim();

            Optional<Ingredient> foundIngrednient = Optional.empty();
            if(ingredientName != null && !ingredientName.isEmpty()) {
                foundIngrednient = ingredients.stream()
                        .filter(ingredient -> ingredient.getName().equals(ingredientName))
                        .findFirst();
            }
            if(foundIngrednient.isPresent()){
                newDrinkingredients.add(foundIngrednient.get());
                newDrink.getAmounts().add(ingredientAmount);
                //foundIngrednient.get().getDrinks().add(newDrink);
            }
            else if(ingredientName != null && !ingredientName.isEmpty()) {

                Ingredient ingredient = fetchIngredientByName(ingredientName);

                Thread.sleep(400);

                if(ingredient.getName() == null || ingredient.getName().isEmpty()) ingredient.setName(ingredientName);
                ingredient.setImageURL("www.thecocktaildb.com/images/ingredients/"+ingredient.getName().toLowerCase().replace(" ", "%20")+".png");
                if(ingredient.getIdIngredient() == null) {
                    int tempId = 10000;
                    for (Ingredient tempI : ingredients) {
                        Optional<Ingredient> foundID = ingredients.stream()
                                .filter(ing -> ing.getName().equals(ingredientName))
                                .findFirst();
                        if (foundID.isPresent()) {
                            tempId++;
                        } else {
                            break;
                        }
                    }
                    ingredient.setIdIngredient(tempId);

                }
                //ingredient.getDrinks().add(newDrink);

                ingredients.add(ingredient);
                newDrinkingredients.add(ingredient);

                newDrink.getAmounts().add(ingredientAmount);
            } else {
                break;
            }
        }
        newDrink.setIngredients(newDrinkingredients);

        return newDrink;
    }

    public void fetchAll(){
        int x = 0;
/*        List<Integer> testIdArray = new ArrayList<>(List.of(12518,
                12528,
                12560,
                12562,
                12564)); */
        for(Integer id : tcdbIds){
            try {
                Drink drink = fetchDrinkById(id);
                //sleep for some time to avoid being blocked by the API
                Thread.sleep(100);
                if(drink != null){
                    System.out.println(x++ + " " + drink.getName());
                    int counter = 0;
                    for(Ingredient ing : drink.getIngredients()){
                        System.out.println("------ingredient-----" + "id:" + ing.getIdIngredient() + ", name:" + ing.getName()
                                + ", amount:" + drink.getAmounts().get(counter) + ", type:" + ing.getType() + ", isAlcohol:"
                                + ing.getIsAlcohol() + ", strenght: " + ing.getStrenght() + ", img:" + ing.getImageURL());
                        counter++;
                    }
/*                    System.out.println("/////////////////////////lista wszystkich składników///////////////////////////////////");
                    for(Ingredient ing : ingredients){
                        System.out.println("------list-składnik-----" + ing.getApiID() + " " + ing.getName() + " " +
                                ing.getType() + " " + ing.getIsAlcohol() + " " + ing.getStrenght());
                    }
                    System.out.println("//////////////////////////////////////////////////////////////////////////////////////");     */

                    drinks.add(drink);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Ingredient fetchIngredientByName(String name) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(IngredientFinderURL + name)
                .build();
        try {
            Response response = client.newCall(req).execute();
            if(!response.isSuccessful()) {
                throw new IOException("Request failed with HTTP error code: " + response.code());
            }
            ResponseBody body = response.body();
            if(body != null) {
                String rawData = body.string();
                return parseIngredient(rawData); //!!!
            } else {
                throw new IOException("Response body is null");
            }

        }catch (IOException e) {
            throw new FetchException("Failed to fetch ingredient data", e);
        }
    }
        public Ingredient parseIngredient(String rawData){
            JSONObject ingredientData = new JSONObject(rawData);
            if (ingredientData.has("ingredients") && ingredientData.get("ingredients") instanceof JSONArray) {
                JSONArray ingredientArray = ingredientData.getJSONArray("ingredients");
                JSONObject ingredientObject = ingredientArray.getJSONObject(0);
                Ingredient ingredient = new Ingredient();
                ingredient.setIdIngredient(Integer.valueOf(ingredientObject.getString("idIngredient")));
                ingredient.setName(ingredientObject.getString("strIngredient"));
                ingredient.setDescription(ingredientObject.optString("strDescription"));
                ingredient.setType(ingredientObject.optString("strType"));
                ingredient.setIsAlcohol(ingredientObject.getString("strAlcohol"));
                ingredient.setStrenght(ingredientObject.optString("strABV"));

                return ingredient;
            }
            else{
                return new Ingredient();
            }
        }
}
