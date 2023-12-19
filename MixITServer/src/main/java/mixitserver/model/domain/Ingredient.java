package mixitserver.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient implements Serializable{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIngredient;
    //private Integer apiID;
    private String name;
    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;
    @Column(nullable = true)
    private String type;
    @Column(nullable = true)
    private String isAlcohol;
    @Column(nullable = true)
    private String strenght;
    //@Column(nullable = true)
    //private String amount;
    private String imageURL;
    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    private List<Drink> drinks = new ArrayList<>();
}
