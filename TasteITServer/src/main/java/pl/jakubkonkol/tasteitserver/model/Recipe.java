package pl.jakubkonkol.tasteitserver.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Recipe {
    @NotEmpty(message = "Steps cannot be empty")
    private Map<
        @NotNull(message = "Step number cannot be null") Integer,
        @NotBlank(message = "Step description cannot be blank") @Size(max = 500, message = "Description cannot be longer than 500 characters") String
    > steps = new HashMap<>();
    private Map<
        @NotNull(message = "Picture number cannot be null") Integer,
        @NotBlank(message = "Picture URL cannot be blank") String
    > pictures = new HashMap<>();
    @NotEmpty(message = "At least one ingredient is required")
    @Valid
    private List<IngredientWrapper> ingredientsWithMeasurements = new ArrayList<>();
}
