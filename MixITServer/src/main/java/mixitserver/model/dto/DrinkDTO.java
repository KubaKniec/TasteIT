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
    private List<String> instructions;
    private boolean isAlcoholic;
    private String glassType;
    private String image;
    private String category;
    private List<IngredientDTO> ingredients;
}

//jak coś to usunęliśmy pola z relacjami w obiektach dto i ich powinniśmy używać w metodach z serwisów
//też przy tych metodach save, bo jak będziemy chcieli utworzyć nowy drink podając samą encję jako argument to musimy podawać od razu składniki
//a jak będziemy podawać dto drinków to nie trzeba podawać składników, tylko później, jak chcemy to je ustawimy
//w skórcie, metoda np. save powinna zwracać i przyjmować dto, później w środku metody zamieniamy dto na encje i zapisujemy w repo i później znowu zamieniamy na dto żeby zwrócić

// dobra UPDATE:
// wtedy dostawaliśmy stack overflow przy pobieraniu drinku, ponieważ  próbowaliśmy zwracać encję Drink w swojej metodzie findDrinkByIdDrink,
// a to prowadziło do stack overflow, to wynika to z rekurencyjnej relacji pomiędzy Drink a Ingredient, gdzie Drink zawiera listę Ingredient,
// a każdy Ingredient zawiera odwołanie do Drink.
//Więc aby tego uniknąć możemy dodać pole ingredients tutaj w dto i zwracać je również w serwisie i będzie to działało po IngredientDTO nie ma żadnego pola drinkId,
// przez co nie będzie rekurencji