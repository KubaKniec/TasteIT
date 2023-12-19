package mixitserver.repository;

import mixitserver.model.domain.Drink;
import mixitserver.model.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    @Query("SELECT DISTINCT i FROM Ingredient i WHERE i.name LIKE %:name% ORDER BY i.name")
    List<Ingredient> findByName(@Param("name") String name);

    @Query("SELECT d FROM Drink d JOIN d.ingredients i WHERE i.idIngredient = :desiredIngredientId")
    List<Drink> findAllDrinksByIngredient(@Param("desiredIngredientId") int desiredIngredientId);
}
