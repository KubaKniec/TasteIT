package pl.jakubkonkol.testeitserver.repository;

import pl.jakubkonkol.testeitserver.model.Badge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BadgeRepository extends MongoRepository<Badge, String> {
}
