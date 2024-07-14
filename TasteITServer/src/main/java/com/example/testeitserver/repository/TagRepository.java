package com.example.testeitserver.repository;

import com.example.testeitserver.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<Tag, String> {
}
