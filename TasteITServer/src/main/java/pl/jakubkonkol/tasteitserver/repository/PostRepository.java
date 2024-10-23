package pl.jakubkonkol.tasteitserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.jakubkonkol.tasteitserver.model.Like;
import pl.jakubkonkol.tasteitserver.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findByPostMediaTitleContainingIgnoreCaseAndIsAlcoholic(String title,Boolean isAlcoholic, Pageable pageable);
    @Query("{ 'tags.tagName': { $regex: '^?0$', $options: 'i' } }")
    Page<Post> findByTagNameIgnoreCase(String tagName, Pageable pageable);
    List<Post> findByLikesIn(List<Like> likes);
    @Query("{ 'likes': { $exists: true, $ne: [] } }")
    List<Post> findByLikesNotEmpty();
    @Query("{ 'comments': { $exists: true, $ne: [] } }")
    List<Post> findByCommentsNotEmpty();
}
