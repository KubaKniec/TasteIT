package pl.jakubkonkol.tasteitserver.dto;

import lombok.Data;
import pl.jakubkonkol.tasteitserver.model.PostMedia;
import pl.jakubkonkol.tasteitserver.model.enums.PostType;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserProfileDto {
    private String bio;
    private String displayName;
    private String profilePicture;
    private LocalDate birthDate;
}
