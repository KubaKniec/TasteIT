package pl.jakubkonkol.tasteitserver.model.value;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.service.BadgeDataCollector;

@Document
@Getter
@Setter
@NoArgsConstructor
public class BadgeBlueprint {
    private static int nextId = 1;

    @Id
    private int id;
    private String badgeName;
    private String description;
    private String imageUrl;
    private Integer goalValue;
    private Function<BadgeDataCollector,Integer> valueCounter;

    public BadgeBlueprint(String badgeName, String description, String imageUrl, Integer goalValue, Function<BadgeDataCollector,Integer> valueCounter) {
        id = nextId++;
        this.badgeName = badgeName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.goalValue = goalValue;
        this.valueCounter = valueCounter;
    }

    public int countValue(BadgeDataCollector collector) {
        if (valueCounter == null) {
            return 0;
        }
        return valueCounter.apply(collector);
    }

    public void setValueCounter(Function<BadgeDataCollector,Integer> valueCounter) {
        this.valueCounter = valueCounter;
    }
}
