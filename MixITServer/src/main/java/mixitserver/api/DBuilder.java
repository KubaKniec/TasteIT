package mixitserver.api;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mixitserver.service.DrinkService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBuilder {
    private final DrinkService drinkService;
    //@PostConstruct
    public void buildDataBase(){
        Fetcher f = new Fetcher();
        f.fetchAll();
        drinkService.saveAll(f.getDrinks());
    }
}
