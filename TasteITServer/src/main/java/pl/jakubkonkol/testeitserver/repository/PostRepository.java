package pl.jakubkonkol.testeitserver.repository;

import pl.jakubkonkol.testeitserver.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

}
