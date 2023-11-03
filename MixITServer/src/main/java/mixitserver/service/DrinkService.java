package mixitserver.service;

import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.repository.DrinkRepository;
import mixitserver.service.mapper.DrinkMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DrinkService {
    private final DrinkRepository drinkRepository;

    public Drink save(Drink drink) {
        return drinkRepository.save(drink);
    }
    public void saveAll(ArrayList<Drink> drinks){
        for(Drink drink : drinks){
            save(drink);
        }
    }

}
