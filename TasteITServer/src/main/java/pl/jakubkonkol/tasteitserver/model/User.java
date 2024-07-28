package pl.jakubkonkol.tasteitserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Data
public class User implements UserDetails {
    @Id
    private String user_id;
    private String email;
    private String displayName;
    private String bio;
    private String profilePicture;
    private Date createdAt;
    private Date birthDate;
    private Authentication authentication;
    private List<String> roles = List.of("USER");

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> (GrantedAuthority) () -> r).toList();
    }

    /**
     * Tutaj trzeba dodac brakujace pola
     */



    @Override
    public String getPassword() {
        return authentication.getPassword();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
