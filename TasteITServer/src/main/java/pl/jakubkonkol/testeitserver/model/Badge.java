package pl.jakubkonkol.testeitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Badge {
    @Id
    private String badgeId;
    private String badgeName;
    private String description;
    private String imageUrl;
}
