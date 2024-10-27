package pl.jakubkonkol.tasteitserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDto {
    @NotBlank(message = "User Id name cannot be blank")
    private String userId;
//    @Size(max = 50, message = "Display name cannot exceed 50 characters") // idk if we should add that?
    @NotBlank(message = "Bio cannot be blank")
    private String bio;
    @NotBlank(message = "Display name cannot be blank")
    private String displayName;
    @NotBlank(message = "Profile picture cannot be blank")
    private String profilePicture;
    @NotNull(message = "Birth date cannot be null")         //add validation date format
    private LocalDate birthDate;
}
