package pl.jakubkonkol.tasteitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "userActions")
public class UserAction {
    @Id
    private String id;
    private String userId;
    private String actionType;
    private String postId; // ID posta, którego dotyczy akcja
    //    private String commentContent; // Opcjonalne: treść komentarza
    private Date timestamp;
}
