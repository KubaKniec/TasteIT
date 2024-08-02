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

    @GetMapping("/search")
    public ResponseEntity<List<IngredientDto>> ingedientList(@RequestParam String name) {
        return ResponseEntity.ok(ingredientService.searchByName(name));
    }

    //TODO: Nie działa, pomimo wpisania dobrego id wyskakuje 500 no value present
    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable String ingredientId) {
        IngredientDto ingredientDto = ingredientService.getIngredient(ingredientId);
        if (ingredientDto == null) {
            throw new ResourceNotFoundException("Ingredient not found with Id: " + ingredientId);
        }
        return ResponseEntity.ok(ingredientDto);
    }

    @GetMapping("/getAll")
    public  ResponseEntity<List<IngredientDto>> getAll() {
        return ResponseEntity.ok(ingredientService.getAll());

    }
}

