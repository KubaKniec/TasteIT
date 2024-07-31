package pl.jakubkonkol.tasteitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class IngredientWrapper {
    private Ingredient ingredient;
    private Measurement measurement;
}
