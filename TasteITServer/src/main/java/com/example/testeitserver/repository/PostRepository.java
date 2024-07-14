package com.example.testeitserver.repository;

import com.example.testeitserver.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

}
