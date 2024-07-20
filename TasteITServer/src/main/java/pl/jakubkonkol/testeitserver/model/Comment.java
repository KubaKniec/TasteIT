package pl.jakubkonkol.testeitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Comment {
    @Id
    private String commentId;
    private String postId;
    private String userId;
    private String content;
    private Date date;
}
