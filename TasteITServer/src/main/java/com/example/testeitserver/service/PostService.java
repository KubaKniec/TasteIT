package com.example.testeitserver.service;

import com.example.testeitserver.model.Post;
import com.example.testeitserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

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
