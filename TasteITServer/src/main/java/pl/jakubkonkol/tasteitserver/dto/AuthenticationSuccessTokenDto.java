package pl.jakubkonkol.tasteitserver.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationSuccessTokenDto {
    private String sessionToken;
}
