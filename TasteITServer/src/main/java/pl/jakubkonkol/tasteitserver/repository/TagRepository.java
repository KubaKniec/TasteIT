package pl.jakubkonkol.tasteitserver.repository;

import pl.jakubkonkol.tasteitserver.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findByTagName(String name);
}
