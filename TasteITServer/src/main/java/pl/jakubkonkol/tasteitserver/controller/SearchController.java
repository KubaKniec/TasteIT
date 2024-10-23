package pl.jakubkonkol.tasteitserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubkonkol.tasteitserver.dto.PageDto;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.dto.TagDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.service.PostService;
import pl.jakubkonkol.tasteitserver.service.TagService;
import pl.jakubkonkol.tasteitserver.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final PostService postService;
    private final UserService userService;
    private final TagService tagService;

    @GetMapping("/posts")
    public ResponseEntity<PageDto<PostDto>> searchPosts(@RequestParam String query,
                                                        @RequestParam(required = false) Boolean isAlcoholic,
                                                        @RequestHeader("Authorization") String sessionToken,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size) {
        PageDto<PostDto> pageDto = postService.searchPosts(query, isAlcoholic, sessionToken, page, size);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping("/users")
    public ResponseEntity<PageDto<UserReturnDto>> searchUsers(@RequestParam String query,
                                                        @RequestHeader("Authorization") String sessionToken,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size) {
        PageDto<UserReturnDto> pageDto = userService.searchUsersByDisplayName(query, sessionToken, page, size);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDto>> searchTags(@RequestParam String query) {
        List<TagDto> tagDtos = tagService.searchTagsByName(query);
        return ResponseEntity.ok(tagDtos);
    }

    @GetMapping("/tags/posts")
    public ResponseEntity<PageDto<PostDto>> searchPostsByTag(@RequestParam String query,
                                                             @RequestHeader("Authorization") String sessionToken,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        PageDto<PostDto> pageDto = postService.searchPostsByTagName(query, sessionToken, page, size);
        return ResponseEntity.ok(pageDto);
    }
}
