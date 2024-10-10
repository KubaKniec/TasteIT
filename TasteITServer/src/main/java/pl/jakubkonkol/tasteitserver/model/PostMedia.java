package pl.jakubkonkol.tasteitserver.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostMedia {
    @NotNull(message = "Title cannot be null.")
    @NotBlank(message = "Title cannot be blank.")
    private String title;
    private String description;
    @NotEmpty(message = "At least one picture is required.")
    private List<String> pictures = new ArrayList<>();
}
