package com.example.testeitserver.repository;

import com.example.testeitserver.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, String> {
}
