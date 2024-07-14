package com.example.testeitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Tag {
    @Id
    private String tagId;
    private String tagName;
}
