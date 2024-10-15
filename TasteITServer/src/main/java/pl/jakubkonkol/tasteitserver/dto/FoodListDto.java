package pl.jakubkonkol.tasteitserver.dto;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.jakubkonkol.tasteitserver.model.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodListDto {
    @Id
    public String foodListId;
    public String userId;
    @CreatedDate
    public Date createdDate;
    @DBRef
    public List<Post> postsList = new ArrayList<>();;
}
