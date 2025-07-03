package pl.jakubkonkol.tasteitserver.repository;

import pl.jakubkonkol.tasteitserver.model.Badge;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.jakubkonkol.tasteitserver.model.value.BadgeBlueprint;

public interface BadgeRepository extends MongoRepository<BadgeBlueprint, String> {
}
