package pl.jakubkonkol.testeitserver.service;

import pl.jakubkonkol.testeitserver.model.Post;
import pl.jakubkonkol.testeitserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    public void save(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null.");
        }
        postRepository.save(post);
    }
    public void saveAll(List<Post> posts) {
        if (posts == null) {
            throw new IllegalArgumentException("List of posts cannot be null.");
        }
        postRepository.saveAll(posts);
    }

    public void deleteAll() {
        postRepository.deleteAll();
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }
}
