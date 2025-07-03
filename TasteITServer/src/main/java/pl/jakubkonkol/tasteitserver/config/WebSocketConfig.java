package pl.jakubkonkol.tasteitserver.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final UserRepository userRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // This configures the message routing paths:
        // /topic is used for broadcasts (like global notifications)
        // /user is for private messages (like personal notifications)
        config.enableSimpleBroker("/topic", "/user");

        // When clients send messages to the server, they'll start with /app
        config.setApplicationDestinationPrefixes("/app");

        // This prefix is used for user-specific subscriptions
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // This creates the WebSocket endpoint that clients will connect to
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200");  // frontend URL
//                .withSockJS();  // Provides fallback options for older browsers
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class);

                System.out.println("Received message: " + message);
                assert accessor != null;
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String sessionToken = accessor.getFirstNativeHeader("Authorization");

                    if (sessionToken != null) {
                        Optional<User> userOptional = userRepository.findBySessionToken(sessionToken);

                        if (userOptional.isEmpty()) {
                            System.out.println("User not found for token: " + sessionToken);
                            throw new IllegalArgumentException("Invalid session token");
                        }

                        User user = userOptional.get();
                        accessor.setUser(new Principal() {
                            @Override
                            public String getName() {
                                return user.getUserId();
                            }
                        });
                    } else {
                        System.out.println("No Authorization header received");
                        throw new IllegalArgumentException("No Authorization header");
                    }
                }
                return message;
            }
        });
    }
}