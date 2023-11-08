package mixitserver.repository;

import mixitserver.model.domain.Drink;
import mixitserver.model.domain.Ingredient;
import mixitserver.model.enums.IngredientMatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Integer> {
    @Query("SELECT drink FROM Drink drink WHERE " +
            "(:category IS NULL OR drink.category = :category) " +
            "AND (:isAlcoholic IS NULL OR drink.isAlcoholic = :isAlcoholic) " +
            "AND (:glassType IS NULL OR drink.glassType = :glassType)")
    List<Drink> filterDrinks(@Param("category")String category, @Param("isAlcoholic") Boolean isAlcoholic, @Param("glassType") String glassType);
    List<Drink> findTop10ByOrderByPopularityDesc();
    //List<Drink> findAllByNameOrderByPopularityDesc(String drinkName);
//    List<Drink> findByNameContainingOrderByPopularityDesc(String drinkName);
    List<Drink> findByNameContainingIgnoreCaseOrderByPopularityDesc(String drinkName);

    @Query("SELECT drink FROM Drink drink WHERE " +
            "(:category IS NULL OR drink.category = :category) " +
            "AND (:isAlcoholic IS NULL OR drink.isAlcoholic = :isAlcoholic) " +
            "AND (:glassType IS NULL OR drink.glassType = :glassType) " +
            "AND (:matchType = 'ALL' AND " +
            "  NOT EXISTS (" +
            "    SELECT i FROM Ingredient i " +
            "    WHERE i.drink = drink AND i.name NOT IN :ingredientNames" +
            "  ) " +
            "  OR :matchType = 'AT_LEAST' AND " +
            "  (SELECT COUNT(DISTINCT i.name) FROM Ingredient i WHERE i.drink = drink AND i.name IN :ingredientNames) >= :minIngredientCount " +
            "  OR :matchType NOT IN ('ALL', 'AT_LEAST'))")
    List<Drink> findDrinksWithFilters(@Param("category") String category,
                             @Param("isAlcoholic") Boolean isAlcoholic,
                             @Param("glassType") String glassType,
                             @Param("matchType") String matchType,
                             @Param("ingredientNames") List<String> ingredientNames,
                             @Param("minIngredientCount") Integer minIngredientCount);


}
