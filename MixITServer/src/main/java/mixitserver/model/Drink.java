package mixitserver.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDrink;
    private Integer apiId;
    private String name;
    @OneToMany(mappedBy = "drink", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();
    private String instructions;
    private boolean isAlcoholic;
    private String glassType;
    private String image;
    private String category;
}
