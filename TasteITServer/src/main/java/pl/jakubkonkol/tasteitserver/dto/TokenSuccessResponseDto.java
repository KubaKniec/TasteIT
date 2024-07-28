package pl.jakubkonkol.tasteitserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenSuccessResponseDto {
    private String accessToken;
}
