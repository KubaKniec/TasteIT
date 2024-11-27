package pl.jakubkonkol.tasteitserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClusteringService {
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public void requestClustering() {
        String correlationId = UUID.randomUUID().toString();

        List<PostDto> posts = postRepository.findAll().stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();

        Map<String, Object> payload = new HashMap<>();
        payload.put("posts", posts);
        payload.put("correlationId", correlationId);

        String requestTopic = "clustering-request";
        kafkaTemplate.send(requestTopic, correlationId, payload);
    }

    @KafkaListener(topics = "clustering-response", groupId = "${spring.kafka.consumer.group-id}")
    public void handleClusteringResponse(@Payload Map<String, Object> response) {
        String correlationId = (String) response.get("correlationId");
        System.out.println("Received response for correlationId: " + correlationId);
        System.out.println("Clusters: " + response.get("clusters"));
        System.out.println("Post assignments: " + response.get("posts_assignments"));
    }
}