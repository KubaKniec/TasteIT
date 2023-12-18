package mixitserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mixitserver.model.domain.Drink;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO {
    private Integer idIngredient;
    private String name;
    private String description;
    private String type;
    private String isAlcohol;
    private String strenght;
    private String amount;
    private String imageURL;
    private List<Drink> drinks = new ArrayList<>();
}
