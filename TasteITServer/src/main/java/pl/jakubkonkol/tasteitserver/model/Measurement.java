package pl.jakubkonkol.tasteitserver.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Measurement {
    @NotBlank(message = "Unit cannot be blank")
    private String unit;
    @NotBlank(message = "Value cannot be blank")
    @Size(max = 20, message = "Value cannot be longer than 20 characters")
    private String value;
}
