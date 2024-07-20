package pl.jakubkonkol.testeitserver.repository;

import pl.jakubkonkol.testeitserver.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<Tag, String> {
}
