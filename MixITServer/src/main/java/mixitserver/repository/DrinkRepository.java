package mixitserver.repository;

import mixitserver.model.domain.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Integer> {
//    Drink findDrinkByIdDrink(Integer id);
//    Drink findDrinkByIdDrink(Integer id);

    List<Drink> findAll();
}
