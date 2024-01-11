package mixitserver.service;

import lombok.RequiredArgsConstructor;
import mixitserver.jwt.JwtTokenProvider;
import mixitserver.model.domain.User;
import mixitserver.model.dto.UserDto;
import mixitserver.repository.UserRepository;
import mixitserver.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto getUserInfo(Long idUser) {
        User user = userRepository.findByIdUser(idUser)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + idUser));

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getActualUsername());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public UserDto updateUserInfo(Long idUser, UserDto updatedUserDto) {
        User existingUser = userRepository.findByIdUser(idUser)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + idUser));

        if (!existingUser.getEmail().equals(updatedUserDto.getEmail()) && userRepository.existsByEmail(updatedUserDto.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        if (!existingUser.getActualUsername().equals(updatedUserDto.getUsername()) && userRepository.existsByUsername(updatedUserDto.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        existingUser.setUsername(updatedUserDto.getUsername());
        existingUser.setEmail(updatedUserDto.getEmail());

        User updatedUser = userRepository.save(existingUser);

        String newToken = jwtTokenProvider.generateToken(updatedUser);

        UserDto responseDto = UserMapper.toDto(updatedUser);
        responseDto.setToken(newToken);

        return responseDto;
    }
}
