package pl.jakubkonkol.tasteitserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GenericResponse {
    private int status;
    private String message;
}
