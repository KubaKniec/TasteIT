package mixitserver.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBar;
    private String name;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Bar_Drink",
            joinColumns = { @JoinColumn(name = "idBar") },
            inverseJoinColumns = { @JoinColumn(name = "idDrink") }
    )
    private List<Drink> drinks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
