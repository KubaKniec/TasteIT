package mixitserver.service.mapper;

import mixitserver.model.domain.User;
import mixitserver.model.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setIdUser(user.getIdUser());
        userDto.setUsername(user.getActualUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRoles().iterator().next());
        return userDto;
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        return user;
    }
}
