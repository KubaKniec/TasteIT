package mixitserver.model.domain;


import jakarta.persistence.*;
import lombok.*;
import mixitserver.model.domain.Ingredient;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDrink;
    private Integer apiId;
    private String name;
    @OneToMany(mappedBy = "drink", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();
    @Lob
    @Column(length = 100000)
    private String instructions;
    private boolean isAlcoholic;
    private String glassType;
    private String image;
    private String category;
}
