package mixitserver.model.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mixitserver.model.domain.Drink;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDTO {
    private Integer idDrink;
    private String name;
    private String amount;
    @ManyToOne
    @JoinColumn(name = "drink_id") //TODO Do i need to add realations anntation in DTO object
    private Drink drink;
}
