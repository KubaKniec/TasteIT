package mixitserver.controller;

import lombok.RequiredArgsConstructor;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.service.DrinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/")
@RequiredArgsConstructor
public class DrinkController {
    private final DrinkService drinkService;
    @PostMapping("/save")
    public ResponseEntity<DrinkDTO> save(@RequestBody DrinkDTO drink){
        return ResponseEntity.ok(drinkService.save(drink));
    }

}
