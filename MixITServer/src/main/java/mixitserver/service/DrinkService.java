package mixitserver.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mixitserver.model.additional.Filter;
import mixitserver.model.domain.Drink;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.repository.DrinkRepository;
import mixitserver.service.mapper.DrinkMapper;
import mixitserver.service.mapper.DrinkMapperImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrinkService {
    private final DrinkRepository drinkRepository;
    private final DrinkMapperImpl drinkMapper;
    private DrinkDTO dailyDrink;

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

    public DrinkDTO getDrinkByIdDrink(Integer id) {
        Drink drink = drinkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Drink not found with id: " + id));

        if (drink.getPopularity() == null) {
            throw new IllegalStateException("Drink Popularity is null");
        }

        drink.setPopularity(drink.getPopularity() + 1);
        drinkRepository.save(drink);

        return drinkMapper.mapToDto(drink);
    }

    public List<DrinkDTO> getAll() {
        return drinkRepository.findAll()
                .stream()
                .map(drinkMapper::mapToDto)
                .toList();
    }
    public List<DrinkDTO> getTop10DrinksByPopularity() {
        List<Drink> topDrinks = drinkRepository.findTop10ByOrderByPopularityDesc();
        return topDrinks.stream().map(drink -> {
            if (drink.getPopularity() == null) {
                throw new IllegalStateException("Drink Popularity is null");
            }
            return drinkMapper.mapToDto(drink);
        }).toList();
    }

    public List<DrinkDTO> getDrinksByName(String drinkName) {
        List<Drink> drinks = drinkRepository.findByNameContainingIgnoreCaseOrderByPopularityDesc(drinkName);
        return drinks.stream().map(drinkMapper::mapToDto).toList();
    }

    @Scheduled(cron = "0 0 0 * * ?") // Uruchamia co dzień o północy
    public void drawDailyDrink() {
        dailyDrink = getRandomDrink();
    }

    public DrinkDTO getDailyDrink() {
        if (dailyDrink == null) {
            dailyDrink = getRandomDrink();            // jeśli dzienne losowanie jeszcze się nie odbyło, losuj drinka teraz
        }
        return dailyDrink;
    }

    public DrinkDTO getRandomDrink() {                       //TODO póki co random
        Integer numberOfDrinks = getAll().size();
        if (numberOfDrinks == 0) {
            throw new EntityNotFoundException("No drinks available in the database.");
        }

        Random random = new Random();
        Integer randomIndex = random.nextInt(numberOfDrinks);

        return getDrinkByIdDrink(randomIndex);
    }

    public List<DrinkDTO> filterDrinks(Filter filter){
        return drinkRepository.filterDrinks(filter.getCategory(), filter.getIsAlcoholic(), filter.getGlassType()).stream().map(drinkMapper::mapToDto).toList();
    }

}
