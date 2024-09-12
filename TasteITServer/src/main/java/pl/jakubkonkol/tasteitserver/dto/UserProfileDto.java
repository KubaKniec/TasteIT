package pl.jakubkonkol.tasteitserver.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDto {
    private String bio;
    private String displayName;
    private String profilePicture;
    private LocalDate birthDate;
}
