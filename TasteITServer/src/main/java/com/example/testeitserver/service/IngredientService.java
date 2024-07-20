package com.example.testeitserver.service;

import com.example.testeitserver.model.Ingredient;
import com.example.testeitserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public Optional<Ingredient> findByName(String name) {
        return ingredientRepository.findByName(name);
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
