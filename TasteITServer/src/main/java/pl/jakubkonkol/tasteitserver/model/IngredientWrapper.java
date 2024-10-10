package pl.jakubkonkol.tasteitserver.model;

import lombok.Data;

@Data
public class IngredientWrapper {
    private Ingredient ingredient;
    private Measurement measurement;
}