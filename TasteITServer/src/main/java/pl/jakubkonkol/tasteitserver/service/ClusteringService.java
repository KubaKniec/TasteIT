package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ClusteringService {
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final Map<String, CompletableFuture<Map<String, Object>>> pendingRequests = new ConcurrentHashMap<>();

    @Value("${kafka.topic.request}")
    private String requestTopic;

    @Value("${kafka.topic.response}")
    private String responseTopic;

    public CompletableFuture<Map<String, Object>> requestTopicAnalysis() {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        List<Post> all = postRepository.findAll();
        List<PostDto> posts = all.stream().map(post -> modelMapper.map(post, PostDto.class)).toList();

        Map<String, Object> payload = new HashMap<>();
        payload.put("posts", posts);

        Message<Map<String, Object>> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                .setHeader(KafkaHeaders.TOPIC, requestTopic)
                .build();

        kafkaTemplate.send(message);

        scheduleFutureTimeout(correlationId, future, 30);

        return future;
    }

    @KafkaListener(topics = "${kafka.topic.response}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleAnalysisResponse(
            @Payload Map<String, Object> response,
            @Header(KafkaHeaders.CORRELATION_ID) String correlationId
    ) {
        CompletableFuture<Map<String, Object>> future = pendingRequests.remove(correlationId);
        if (future != null) {
            future.complete(response);
        }
    }

    private void scheduleFutureTimeout(String correlationId, CompletableFuture<Map<String, Object>> future, int timeoutSeconds) {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            CompletableFuture<Map<String, Object>> pendingFuture = pendingRequests.remove(correlationId);
            if (pendingFuture != null) {
                pendingFuture.completeExceptionally(new TimeoutException("Analysis request timed out"));
            }
        }, timeoutSeconds, TimeUnit.SECONDS);
    }
}