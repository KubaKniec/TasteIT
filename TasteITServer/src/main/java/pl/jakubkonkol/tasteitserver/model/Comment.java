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
public class Comment {
    @Id
    private String commentId;
    private String postId;
    private String userId;
    private String content;
    @CreatedDate
    private Date date;
}
