package mixitserver.service;

import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.model.dto.IngredientDTO;
import mixitserver.repository.IngredientRepository;
import mixitserver.service.mapper.DrinkMapperImpl;
import mixitserver.service.mapper.IngredientMapper;
import mixitserver.service.mapper.IngredientMapperImpl;
import org.springframework.stereotype.Service;
import mixitserver.model.domain.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapperImpl ingredientMapper;
    private final DrinkMapperImpl drinkMapper;
    public List<IngredientDTO> getAll() {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredientMapper::mapToDto)
                .toList();
    }
    public IngredientDTO getById(int id) {
        return ingredientMapper.mapToDto(ingredientRepository.findById(id).orElseThrow());
    }

    public List<IngredientDTO> searchByname(String name) {
        return ingredientRepository.findByName(name)
                .stream()
                .collect(Collectors.toMap(Ingredient::getName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .map(ingredientMapper::mapToDto)
                .toList();
    }

    public void saveAll(ArrayList<Ingredient> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("List of drinks cannot be null.");
        }

        for (Ingredient ingredient : ingredients) {
            if (ingredient == null) {
                throw new IllegalArgumentException("Drink cannot be null.");
            }
            ingredientRepository.save(ingredient);
        }
    }

    public List<DrinkDTO> searchDrinksFromIngredientsById(int id){
        return ingredientRepository.findAllDrinksByIngredient(id)
                .stream().map(drinkMapper::mapToDto).toList();
        //return null;
    }
}
