package pl.jakubkonkol.tasteitserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Provided email has an invalid format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp =
            "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[!#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~])"
            + "(?=\\S+$).{6,64}$"
            , message = "Provided password has an invalid format. " +
            "Password must be: 6-64 characters long, contains at least one digit, one lowercase letter, " +
            "one uppercase letter, one special character '  ', and has no whitespace")
    private String password;


}

