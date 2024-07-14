package com.example.testeitserver.repository;

import com.example.testeitserver.model.PostMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostMediaRepository extends MongoRepository<PostMedia, String> {
}
