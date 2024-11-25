package pl.jakubkonkol.tasteitserver.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;
import pl.jakubkonkol.tasteitserver.service.ClusteringService;
import pl.jakubkonkol.tasteitserver.service.PostService;
import pl.jakubkonkol.tasteitserver.service.RankerService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {
    private final RankerService rankerService;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ClusteringService clusteringService;

    @GetMapping("/rankedfeed")
    public ResponseEntity<List<Post>> getRankedFeed(@RequestHeader("Authorization") String sessionToken) {
        List<Post> all = postRepository.findTop100ByOrderByCreatedAtDesc();
        List<Post> posts = rankerService.rankPosts(all, sessionToken);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/allposts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<Post> all = postRepository.findAll();
        List<PostDto> list = all.stream().map(post -> modelMapper.map(post, PostDto.class)).toList();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/request_clustering")
    public ResponseEntity<?> analyzeTopics() {
        try {
            CompletableFuture<Map<String, Object>> result = clusteringService.requestTopicAnalysis();
            Map<String, Object> analysisResult = result.get(30, TimeUnit.SECONDS);
            return ResponseEntity.ok(analysisResult);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to analyze topics: " + e.getMessage()));
        }
    }
}
