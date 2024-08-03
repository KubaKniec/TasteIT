package pl.jakubkonkol.tasteitserver.repository;

import pl.jakubkonkol.tasteitserver.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    Optional<Ingredient> findByName(String name);

    List<Ingredient> findIngredientByNameContainingIgnoreCase(String name);

}
