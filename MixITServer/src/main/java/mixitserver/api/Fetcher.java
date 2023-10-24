package mixitserver.api;

import org.springframework.stereotype.Component;

@Component
public class Fetcher {
    public String URL = "www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";
    void fetchDrinkById(int id){
        String url = this.URL+id;

    }
}
