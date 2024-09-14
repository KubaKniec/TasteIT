package pl.jakubkonkol.tasteitserver.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@Builder
public class Like {
    @Id
    private String likeId;
    private String postId;
    private String userId;
    @CreatedDate
    private Date date;
}
