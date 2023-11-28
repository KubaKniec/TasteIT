package mixitserver.controller;

import lombok.RequiredArgsConstructor;
import mixitserver.repository.DrinkRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/v1/public/glassTypes")
@RequiredArgsConstructor
public class GlassController {
    private final DrinkRepository drinkRepository;

    @GetMapping("/getAll")
    public Set<String> getAllGlassTypes() {
        return drinkRepository.findAllGlassTypes();
    }
}