package mixitserver.service;

import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.repository.DrinkRepository;
import mixitserver.service.mapper.DrinkMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkService {
    private final DrinkRepository drinkRepository;

    public Drink save(Drink drink) {
        return drinkRepository.save(drink);
    }
    public void saveAll(ArrayList<Drink> drinks){
        drinkRepository.saveAll(drinks);
//        for(Drink drink : drinks){
//            save(drink);
//        }
    }

    public DrinkDTO findDrinkByIdDrink(Integer id) {
//        return drinkRepository.findDrinkByIdDrink(id);
        var optionalDrink = drinkRepository.findById(id);
        if (optionalDrink.isPresent())
            return DrinkMapper.getInstace().mapToDto(drinkRepository.findById(id).get()); //TODO handle this
        return null;
    }

    public List<DrinkDTO> findAll() {
        var optionalDrinks = drinkRepository.findAll();
        List<DrinkDTO> drinkDTOS = new ArrayList<>();
        if (!optionalDrinks.isEmpty()){
            optionalDrinks.forEach(drink -> {
                drinkDTOS.add(DrinkMapper.getInstace().mapToDto(drink));
            });
            return drinkDTOS;
        }
        return null;
    }
}
