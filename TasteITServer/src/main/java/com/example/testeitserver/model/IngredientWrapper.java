package com.example.testeitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class IngredientWrapper {
    @Id
    private String ingredientWrapperId;
    private Ingredient ingredient;
    private Measurement measurement;
}
