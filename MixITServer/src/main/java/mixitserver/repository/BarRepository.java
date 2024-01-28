package mixitserver.repository;

import mixitserver.model.domain.Bar;
import mixitserver.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarRepository extends JpaRepository<Bar, Integer> {

    boolean existsByIdBarAndUserIdUser(Integer barId, Long userId);

    boolean existsByNameAndUserIdUser(String name, Long userId);
}
