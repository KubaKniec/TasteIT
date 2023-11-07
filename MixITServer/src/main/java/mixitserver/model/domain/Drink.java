package mixitserver.model.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import mixitserver.model.domain.Ingredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Drink{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDrink;
    private Integer apiId;
    private String name;
    @OneToMany(mappedBy = "drink", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "drink_instructions", joinColumns = @JoinColumn(name = "drink_id"))
    @Column(name = "instruction", columnDefinition = "TEXT")
    private List<String> instructions;
    private boolean isAlcoholic;
    private String glassType;
    private String image;
    private String category;
//    @Column(columnDefinition = "integer default 0")   // nie działczy :(
    private Integer popularity = 0;     // get request count
}
