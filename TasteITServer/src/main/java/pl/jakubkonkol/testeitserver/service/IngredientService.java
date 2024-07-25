package pl.jakubkonkol.testeitserver.service;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import pl.jakubkonkol.testeitserver.model.Ingredient;
import pl.jakubkonkol.testeitserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public Optional<Ingredient> findByName(String name) {
        List<Ingredient> ingredients = ingredientRepository.findByName(name);
        return ingredients.stream().findFirst();
    }
    public void save(Ingredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null.");
        }
        ingredientRepository.save(ingredient);
    }
    public void saveAll(List<Ingredient> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("List of drinks cannot be null.");
        }
        ingredientRepository.saveAll(ingredients);
    }
    public void deleteAll() {
        ingredientRepository.deleteAll();
    }
    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }
}
