package pl.jakubkonkol.tasteitserver.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import pl.jakubkonkol.tasteitserver.model.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FoodListDto {
    public String foodListId;
    public String name;
    public Date createdDate;
    public List<Post> postsList = new ArrayList<>();
    public int postsCount;
}
