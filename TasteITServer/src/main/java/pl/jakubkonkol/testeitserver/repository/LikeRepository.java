package pl.jakubkonkol.testeitserver.repository;

import pl.jakubkonkol.testeitserver.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, String> {
}
