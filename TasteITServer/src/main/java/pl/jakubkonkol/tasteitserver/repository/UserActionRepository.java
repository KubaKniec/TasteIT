package pl.jakubkonkol.tasteitserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.jakubkonkol.tasteitserver.model.UserAction;

public interface UserActionRepository extends MongoRepository<UserAction, String> {
}
