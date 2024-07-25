package pl.jakubkonkol.tasteitserver.repository;

import pl.jakubkonkol.tasteitserver.model.Badge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BadgeRepository extends MongoRepository<Badge, String> {
}
