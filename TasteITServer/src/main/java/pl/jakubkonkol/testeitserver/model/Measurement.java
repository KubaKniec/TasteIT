package pl.jakubkonkol.testeitserver.model;

import lombok.Data;

@Data
public class Measurement {
    private String unit;
    private String value;
}
