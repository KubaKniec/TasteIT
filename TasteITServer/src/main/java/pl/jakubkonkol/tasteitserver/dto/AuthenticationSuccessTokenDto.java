package pl.jakubkonkol.tasteitserver.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationSuccessTokenDto {
    private String sessionToken;
}
