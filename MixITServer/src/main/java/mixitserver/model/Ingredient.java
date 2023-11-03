package mixitserver.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIngredient;
    private String name;
    private String amount;
    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;
}
