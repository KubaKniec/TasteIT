package pl.jakubkonkol.testeitserver.repository;

import pl.jakubkonkol.testeitserver.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
