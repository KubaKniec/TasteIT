package mixitserver.model.dto;

import lombok.Data;
import mixitserver.model.domain.enums.Role;

import java.util.List;

@Data
public class UserDto {
    private Long idUser;
    private String username;
    private String email;
    private String password;
    private Role role;
    private String token;
    private List<BarDto> bars;
    private List<DrinkDTO> favouriteDrinks;
}
