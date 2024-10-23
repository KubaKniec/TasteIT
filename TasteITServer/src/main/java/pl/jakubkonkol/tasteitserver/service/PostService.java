package pl.jakubkonkol.tasteitserver.service;

import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import pl.jakubkonkol.tasteitserver.dto.PageDto;
import pl.jakubkonkol.tasteitserver.dto.PostDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.Recipe;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.LikeRepository;
import pl.jakubkonkol.tasteitserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    public void save(PostDto postDto) {
        Post post = convertToEntity(postDto);
        postRepository.save(Objects.requireNonNull(post, "Post cannot be null."));
    }

    public void saveAll(List<Post> posts) {
        postRepository.saveAll(Objects.requireNonNull(posts, "List of posts cannot be null."));
    }

    public void deleteAll() {
        postRepository.deleteAll();
    } //TODO niebezpieczne

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public PostDto getPost(String postId, String sessionToken) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + postId + " not found"));
        return convertToDto(post, sessionToken);
    }

    //temp implementation
    public PageDto<PostDto> getRandomPosts(Integer page, Integer size, String sessionToken) {
        // Create a Pageable object for pagination information
        Pageable pageable = PageRequest.of(page, size);

        // Count the total number of posts
        long total = postRepository.count();

        // Create an aggregation to sample random documents from the collection
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sample(size)
        );

        // Execute the aggregation query
        AggregationResults<Post> results = mongoTemplate.aggregate(aggregation, "post", Post.class);
        List<Post> posts = results.getMappedResults();
        if(posts.isEmpty()){
            throw new NoSuchElementException("No random posts found in repository");
        }

        List<PostDto> postDtos = posts.stream()
                .map(post -> convertToDto(post, sessionToken))
                .toList();

        PageImpl<PostDto> pageImpl = new PageImpl<>(postDtos, pageable, total);

        PageDto<PostDto> pageDto = new PageDto<>();
        pageDto.setContent(pageImpl.getContent());
        pageDto.setPageNumber(pageImpl.getNumber());
        pageDto.setPageSize(pageImpl.getSize());
        pageDto.setTotalElements(pageImpl.getTotalElements());
        pageDto.setTotalPages(pageImpl.getTotalPages());

        return pageDto;
    }

    //if title consists few words use '%20' between them in get request
    public PageDto<PostDto> searchPosts(String query, Boolean isAlcoholic, String sessionToken, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> postPage = postRepository.findByPostMediaTitleContainingIgnoreCaseAndIsAlcoholic(query, isAlcoholic, pageable);

        List<PostDto> postDtos = postPage.getContent().stream()
                .map(post -> convertToDto(post, sessionToken))
                .toList();

        return getPostDtoPageDto(page, size, postDtos, postPage);
    }

    public PageDto<PostDto> searchPostsByTagName(String tagName, String sessionToken, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByTagNameIgnoreCase(tagName, pageable);

        List<PostDto> postDtos = postPage.getContent().stream()
                .map(post -> convertToDto(post, sessionToken))
                .toList();

        return getPostDtoPageDto(page, size, postDtos, postPage);
    }

    private static PageDto<PostDto> getPostDtoPageDto(Integer page, Integer size, List<PostDto> postDtos, Page<Post> postPage) {
        PageDto<PostDto> pageDto = new PageDto<>();
        pageDto.setContent(postDtos);
        pageDto.setPageNumber(page);
        pageDto.setPageSize(size);
        pageDto.setTotalElements(postPage.getTotalElements());
        pageDto.setTotalPages(postPage.getTotalPages());
        return pageDto;
    }

    public Recipe getPostRecipe(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("Post with id " + postId + " not found"));
        return post.getRecipe();
    }

    public List<PostDto> getPostsLikedByUser(String userId, String sessionToken) {
        var likes = likeRepository.findByUserId(userId);
        var posts = postRepository.findByLikesIn(likes);

        return posts.stream()
                .map(post->convertToDto(post, sessionToken))
                .toList();
    }

    public PostDto createPost(PostDto postDto, String sessionToken) {
        Post post = convertToEntity(postDto);
        postRepository.save(post);
        return convertToDto(post, sessionToken);
    }

    private PostDto convertToDto(Post post, String sessionToken) {
        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.setLikesCount((long) post.getLikes().size());
        postDto.setCommentsCount((long) post.getComments().size());

        var currentUser = userService.getCurrentUserDtoBySessionToken(sessionToken); //todo optymalizacja dla wielu postow
        var like = likeRepository.findByPostIdAndUserId(post.getPostId(), currentUser.getUserId()); //todo optymalizacja dla wielu postow

        if(like.isEmpty()){
            postDto.setLikedByCurrentUser(false);
            // nie wiem czy ustawialbym to pole w tym miejscu moze np w getRandomPosts i getPost? jest to jednak metoda konkretnie do konwersji
            //" Aby wiedzieć, czy dany użytkownik jest obserwowany przez innego użytkownika,
            // potrzebujesz dodatkowego kontekstu, czyli danych o użytkowniku aktualnie zalogowanym (np. currentUser).
            // Ta informacja nie powinna być dostępna bezpośrednio w metodzie konwertującej."
        }
        else {
            postDto.setLikedByCurrentUser(true);
        }
        return postDto;
    }

    private Post convertToEntity(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }
}