package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.repository.UserActionRepository;
import pl.jakubkonkol.tasteitserver.service.interfaces.IRankerService;
import pl.jakubkonkol.tasteitserver.service.interfaces.IUserService;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankerService implements IRankerService {
    private final UserActionRepository userActionRepository;
    private final IUserService userService;

    public List<Post> rankPosts(List<Post> candidates, String sessionToken) {
        return null;
    }
}
