package pl.jakubkonkol.tasteitserver.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import pl.jakubkonkol.tasteitserver.annotation.RegisterAction;
import pl.jakubkonkol.tasteitserver.model.UserAction;
import pl.jakubkonkol.tasteitserver.service.UserService;

import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
public class UserActionAspect {
    private final MongoTemplate mongoTemplate;
    private final UserService userService;

    @AfterReturning("@annotation(registerAction)")
    public void logAction(JoinPoint joinPoint, RegisterAction registerAction) {
        String userId = userService.getCurrentUserId();
        // Logowanie akcji w MongoDB
        UserAction action = new UserAction();
        action.setActionType(registerAction.actionType());
        action.setUserId(userId);
        action.setTimestamp(new Date());
        Object[] args = joinPoint.getArgs();
        String postId = (String) args[0];
        action.setPostId(postId);

        mongoTemplate.save(action, "userActions");
    }

}
