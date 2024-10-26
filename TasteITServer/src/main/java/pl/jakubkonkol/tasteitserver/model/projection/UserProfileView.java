package pl.jakubkonkol.tasteitserver.model.projection;

import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.Tag;

import java.time.LocalDate;
import java.util.List;

public interface UserProfileView {
    String getUserId();
    String getEmail();
    String getDisplayName();
    String getBio();
    String getProfilePicture();
    LocalDate getCreatedAt();
    LocalDate getBirthDate();
    Boolean getFirstLogin();
    List<String> getRoles();
    List<String> getFollowers();
    List<String> getFollowing();
}
