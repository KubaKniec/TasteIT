package mixitserver.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.repository.DrinkRepository;
import mixitserver.service.mapper.DrinkMapper;
import mixitserver.service.mapper.DrinkMapperImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrinkService {
    private final DrinkRepository drinkRepository;
    private final DrinkMapperImpl drinkMapper;

    public Drink save(Drink drink) {        //Todo change to dto(?)
        if (drink == null) {
            throw new IllegalArgumentException("Drink cannot be null.");
        }

        return drinkRepository.save(drink);
    }
    public void saveAll(ArrayList<Drink> drinks){   //Todo change to dto(?)
        if (drinks == null) {
            throw new IllegalArgumentException("List of drinks cannot be null.");
        }

        for (Drink drink : drinks) {
            if (drink == null) {
                throw new IllegalArgumentException("Drink cannot be null.");
            }
            drinkRepository.save(drink);
        }

    }

    public DrinkDTO findDrinkByIdDrink(Integer id) {
        var optionalDrink = drinkRepository.findById(id);
        if (optionalDrink.isEmpty()) {
            throw new EntityNotFoundException("Drink not found with id: " + id);
        }
        return optionalDrink.map(drinkMapper::mapToDto).get();
    }

    public List<DrinkDTO> findAll() {
        return drinkRepository.findAll()
                .stream()
                .map(drinkMapper::mapToDto)
                .toList();
    }

}
