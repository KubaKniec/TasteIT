package pl.jakubkonkol.tasteitserver.repository;

import pl.jakubkonkol.tasteitserver.model.Like;
import pl.jakubkonkol.tasteitserver.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByPostMediaTitleContainingIgnoreCase(String title);
    List<Post> findByLikesIn(List<Like> likes);
    @Query("{ 'likes': { $exists: true, $ne: [] } }")
    List<Post> findByLikesNotEmpty();
    @Query("{ 'comments': { $exists: true, $ne: [] } }")
    List<Post> findByCommentsNotEmpty();
}
