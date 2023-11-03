package mixitserver.model.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mixitserver.model.domain.Ingredient;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkDTO {
    private Integer idDrink;
    private Integer apiId;
    private String name;
//    @OneToMany(mappedBy = "drink", cascade = CascadeType.ALL, orphanRemoval = true) //TODO czy powinny byc te pola
//    private List<Ingredient> ingredients = new ArrayList<>();
    private String instructions;
    private boolean isAlcoholic;
    private String glassType;
    private String image;
    private String category;
}
