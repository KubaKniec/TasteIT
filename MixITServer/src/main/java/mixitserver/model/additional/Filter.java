package mixitserver.model.additional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Filter {
    String category;
    Boolean isAlcoholic;
    String glassType;
}
