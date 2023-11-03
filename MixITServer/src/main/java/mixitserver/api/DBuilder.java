package mixitserver.api;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.repository.DrinkRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DBuilder {
    private final DrinkRepository drinkRepository;
    @PostConstruct
    public void buildDataBase(){
        Fetcherv2 f = new Fetcherv2();
        f.fetchAll();
        ArrayList<Drink> drinks = f.getDrinks();
        drinkRepository.saveAll(drinks);
    }
}
