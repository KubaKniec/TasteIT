package mixitserver.repository;

import mixitserver.model.domain.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Integer> {
    List<Drink> findTop10ByOrderByPopularityDesc();
    List<Drink> findAllByNameOrderByPopularityDesc(String drinkName);
}
