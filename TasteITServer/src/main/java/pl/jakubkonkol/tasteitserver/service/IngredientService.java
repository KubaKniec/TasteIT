package pl.jakubkonkol.tasteitserver.service;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import pl.jakubkonkol.tasteitserver.dto.IngredientDto;
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
    private final ModelMapper modelMapper;

    @Cacheable("ingredients")
    public Optional<Ingredient> findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        return ingredientRepository.findByName(name);
    }

    public IngredientDto getIngredient(String ingredientId) {
        if (ingredientId == null) {
            throw new IllegalArgumentException("Id cannot be null.");
        }
        var optionalIngredient = ingredientRepository.findById(ingredientId);
        Ingredient ingredient = optionalIngredient.get();
        return convertToDto(ingredient);
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

    public void deleteById(String ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }
    public List<IngredientDto> getAll() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<IngredientDto> ingredientDtos = ingredients.stream().map(this::convertToDto).toList();
        return ingredientDtos;
    }

    public List<IngredientDto> searchByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        List <Ingredient> ingredientList = ingredientRepository.findIngredientByNameContainingIgnoreCase(name);
        return ingredientList.stream().map(this::convertToDto).toList();

    }

    private IngredientDto convertToDto(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDto.class);
    }
}
