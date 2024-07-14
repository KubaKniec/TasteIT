package com.example.testeitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@Data
public class Post {
    @Id
    private String postId;
    private String userId;
    @DBRef
    private PostMedia postMedia;
    @DBRef
    private Recipe recipe;
    @DBRef
    private List<Tag> tags = new ArrayList<>();
    private Date date;
    @DBRef
    private List<Like> likes = new ArrayList<>();
    @DBRef
    private List<Comment> comments = new ArrayList<>();
}
