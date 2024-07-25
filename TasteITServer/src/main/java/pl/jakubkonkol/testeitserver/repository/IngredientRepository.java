package pl.jakubkonkol.testeitserver.repository;

import org.springframework.data.mongodb.repository.Query;
import pl.jakubkonkol.testeitserver.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    List<Ingredient> findByName(String name);

}
