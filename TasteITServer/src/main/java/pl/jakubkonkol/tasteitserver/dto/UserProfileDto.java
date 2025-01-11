package pl.jakubkonkol.tasteitserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UserProfileDto {
    @NotBlank(message = "User Id name cannot be blank")
    private String userId;
    @Size(max = 500, message = "Bio cannot be longer than 500 characters")
    private String bio;
    @Size(max = 50, message = "Display name cannot be longer than 50 characters")
    @NotBlank(message = "Display name cannot be blank")
    private String displayName;
    @NotBlank(message = "Profile picture cannot be blank")
    private String profilePicture;
    @PastOrPresent(message = "Birth date cannot be in the future")
    private Date birthDate;
}
