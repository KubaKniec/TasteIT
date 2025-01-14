package pl.jakubkonkol.tasteitserver.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IngredientWrapper {
    private String ingredientId;
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 80, message = "Name must be between 1 and 80 characters")
    private String name;
    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;
    private String type;
    private boolean isAlcohol;
    private String strength;
    private String imageURL;
    @NotNull(message = "Measurement cannot be null")
    @Valid
    private Measurement measurement;
}