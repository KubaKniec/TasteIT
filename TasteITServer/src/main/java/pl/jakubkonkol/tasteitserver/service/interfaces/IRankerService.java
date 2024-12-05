package pl.jakubkonkol.tasteitserver.service.interfaces;

import pl.jakubkonkol.tasteitserver.model.Post;

import java.util.Date;
import java.util.List;

public interface IRankerService {
    List<Post> rankPosts(List<Post> candidates, String sessionToken);
}
