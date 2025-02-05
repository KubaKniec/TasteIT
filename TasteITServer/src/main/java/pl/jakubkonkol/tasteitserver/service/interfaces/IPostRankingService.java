package pl.jakubkonkol.tasteitserver.service.interfaces;

import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.User;

import java.util.List;

public interface IPostRankingService {
    List<Post> getRankedPostsForUser(User currentUser, String userId);
    void clearRankedPostsCacheForUser(String userId);
}
