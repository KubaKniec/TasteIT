package pl.jakubkonkol.tasteitserver.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Data
public class User implements UserDetails {
    @Id
    private String userId;
    private String email;
    private String displayName = "guest";
    private String bio = "";
    private String profilePicture = "deafult-pic-id"; //TODO podmienić potem id jak już będziemy mieli foto
    @CreatedDate
    private Date createdAt;
    private Date birthDate = new Date();
    private Authentication authentication;
    private Boolean firstLogin = true;
    private List<String> roles = List.of("USER");
    @DBRef
    private List<Tag> tags = new ArrayList<>();
    private List<String> followers = new ArrayList<>();
    private List<String> following = new ArrayList<>();
    private List<FoodList> foodLists = new ArrayList<>();
    @DBRef
    private List<Post> posts = new ArrayList<>();   //ustawiac przy budowaniu bazy

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> (GrantedAuthority) () -> r).toList();
    }

    @Override
    public String getPassword() {
        return authentication.getPassword();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
