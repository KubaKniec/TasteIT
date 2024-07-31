package pl.jakubkonkol.tasteitserver.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import pl.jakubkonkol.tasteitserver.model.Ingredient;
import pl.jakubkonkol.tasteitserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    @Cacheable("ingredients")
    public Optional<Ingredient> findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        return ingredientRepository.findByName(name);
    }

    public List<Ingredient> searchByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        return ingredientRepository.findIngredientByNameContainingIgnoreCase(name);
    }

    public void save(Ingredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null.");
        }
        if(ingredientRepository.findByName(ingredient.getName()).isPresent()){
            return;
        }
        ingredientRepository.save(ingredient);
    }
    public void saveAll(List<Ingredient> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("List of drinks cannot be null.");
        }
        ingredients.forEach(ingredient -> {
            if (ingredient == null) {
                throw new IllegalArgumentException("Ingredient cannot be null.");
            }
            save(ingredient);
        });
    }
    public void deleteAll() {
        ingredientRepository.deleteAll();
    }
    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }
}
