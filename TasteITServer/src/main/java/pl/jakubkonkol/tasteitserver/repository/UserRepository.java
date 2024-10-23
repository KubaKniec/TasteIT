package pl.jakubkonkol.tasteitserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.jakubkonkol.tasteitserver.model.FoodList;
import pl.jakubkonkol.tasteitserver.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>{
    Optional<User> findByEmail(String email);
    @Query("{ 'authentication.sessionToken' : ?0 }")
    Optional<User> findBySessionToken(String sessionToken);
    Page<User> findByUserIdIn(List<String> followers, Pageable pageable);
    Page<User> findByDisplayNameContainingIgnoreCase(String displayName, Pageable pageable);
}
