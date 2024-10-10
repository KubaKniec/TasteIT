package pl.jakubkonkol.tasteitserver.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubkonkol.tasteitserver.apitools.IngredientFetcher;
import pl.jakubkonkol.tasteitserver.model.Authentication;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class AdminUserFactory {
    private final UserRepository userRepository;
    private static final Logger LOGGER = Logger.getLogger(IngredientFetcher.class.getName());
    private final List<String> roles = List.of("ADMIN", "USER");
    public Boolean CreateAdmin() {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()){
            User admin = new User();
            admin.setDisplayName("ADMIN");
            admin.setUserId("0");
            admin.setEmail("admin@tasteit.pl");
            admin.setRoles(roles);
            admin.setAuthentication(Authentication.builder().password("$2a$10$FYMAQC60DKhfjuAoQmYYheaARKwV4mVZDPNdJupmQD8qyKqmQUNau") //password == Admin123$
                    .salt("$2a$10$FYMAQC60DKhfjuAoQmYYhe").build());
            userRepository.save(admin);
            LOGGER.log(Level.INFO, "Admin account created");
            return true;
        }
        LOGGER.log(Level.INFO, "Admin account already exist");
        return false;
    }
}
