package mixitserver.controller;

import lombok.RequiredArgsConstructor;
import mixitserver.model.domain.Drink;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.service.DrinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/demo/")
@RequiredArgsConstructor
public class DemoDrinkController {
    private final DrinkService drinkService;
    @PostMapping("/save")
    public ResponseEntity<Drink> save(@RequestBody Drink drink){
        return ResponseEntity.ok(drinkService.save(drink));
    }

    @GetMapping("/drink/{id}")
    public ResponseEntity<DrinkDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(drinkService.findDrinkByIdDrink(id));
    }

    @GetMapping("/drink/all")
    public ResponseEntity<List<DrinkDTO>> findAll() {
        return ResponseEntity.ok(drinkService.findAll());
    }
}
