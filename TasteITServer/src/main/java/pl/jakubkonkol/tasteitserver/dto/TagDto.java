package pl.jakubkonkol.tasteitserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagDto {
    private String tagId;
    @NotBlank(message = "Tag Name cannot be blank")
    @Size(min = 1, max = 80, message = "Tag Name must be between 1 and 80 characters")
    private String tagName;
}
