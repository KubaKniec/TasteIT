package com.example.testeitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Ingredient {
    @Id
    private String ingredientId;
    private String name;
    private String description;
    private String type;
    private boolean isAlcohol;
    private String strength;
    private String imageURL;
}
