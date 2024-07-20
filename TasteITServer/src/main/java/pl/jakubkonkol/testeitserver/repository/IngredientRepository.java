package pl.jakubkonkol.testeitserver.repository;

import pl.jakubkonkol.testeitserver.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    Optional<Ingredient> findByName(String name);
}
