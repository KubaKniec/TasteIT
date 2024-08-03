package pl.jakubkonkol.tasteitserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubkonkol.tasteitserver.dto.IngredientDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Ingredient;
import pl.jakubkonkol.tasteitserver.service.IngredientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ingredient")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Ingredient ingredient) {
        ingredientService.save(ingredient);
        return ResponseEntity.ok("Ingredient saved successfully, Id " + ingredient.getIngredientId());
    }

    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAll(@RequestBody List<Ingredient> ingredients) {
        ingredientService.saveAll(ingredients);
        return ResponseEntity.ok("Ingredients saved successfully");
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteById(@PathVariable String ingredientId) {
        ingredientService.deleteById(ingredientId);
        return ResponseEntity.ok("Ingredient deleted succesfully, Id: " + ingredientId);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll() {
        ingredientService.deleteAll();
        return ResponseEntity.ok("Ingredients deleted succesfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<IngredientDto>> searchIngredientByName(@RequestParam String name) {
        return ResponseEntity.ok(ingredientService.searchByName(name));
    }

    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable String ingredientId) {
        IngredientDto ingredientDto = ingredientService.getIngredient(ingredientId);
        if (ingredientDto == null) {
            throw new ResourceNotFoundException("Ingredient not found with Id: " + ingredientId);
        }
        return ResponseEntity.ok(ingredientDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<IngredientDto>> getAll() {
        return ResponseEntity.ok(ingredientService.getAll());

    }

    @GetMapping("/findByName")
    public ResponseEntity<Ingredient> findByName(String name) {
        return ResponseEntity.ok(ingredientService.findByName(name).get());
    }
}

