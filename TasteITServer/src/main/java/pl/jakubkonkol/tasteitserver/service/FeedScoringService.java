package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.model.Post;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.model.value.NormalizationValues;
import pl.jakubkonkol.tasteitserver.model.value.ScoredPost;
import pl.jakubkonkol.tasteitserver.service.interfaces.IFeedScoringService;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedScoringService implements IFeedScoringService {
    public static final double BASE_SCORE_WEIGHT = 0.6;  // Podstawowe metryki zaangażowania
    public static final double TIME_BOOST_MAX = 0.3;     // Wpływ świeżości posta
    public static final double SOCIAL_BOOST_MAX = 0.1;   // Wpływ czynników społecznościowych

    // Wagi dla komponentów podstawowego score'a (suma = 1.0)
    public static final double LIKE_WEIGHT = 0.6;      // Waga polubień
    public static final double COMMENT_WEIGHT = 0.4;   // Waga komentarzy

    // Progi czasowe (w godzinach)
    public static final int FRESH_HOURS = 6;     // Posty poniżej tego wieku dostają pełny bonus
    public static final int RECENT_HOURS = 24;   // Posty poniżej tego wieku dostają częściowy bonus

    public List<ScoredPost> calculateScores(List<Post> posts, User currentUser) {
        // Obliczamy wartości do normalizacji na podstawie wszystkich postów
        NormalizationValues normValues = calculateNormalizationValues(posts);
        Date now = new Date();

        // Używamy CompletableFuture dla równoległego obliczania score'ów
        List<CompletableFuture<ScoredPost>> scoringTasks = posts.stream()
                .map(post -> CompletableFuture.supplyAsync(() ->
                        calculatePostScore(post, currentUser, now, normValues)))
                .toList();

        // Czekamy na wszystkie wyniki i sortujemy po score
        return scoringTasks.stream()
                .map(CompletableFuture::join)
                .sorted(Comparator.comparing(ScoredPost::getScore).reversed())
                .toList();
    }

    private NormalizationValues calculateNormalizationValues(List<Post> posts) {
        List<Integer> likesCount = posts.stream()
                .map(post -> post.getLikes().size())
                .collect(Collectors.toCollection(ArrayList::new));

        List<Integer> commentsCount = posts.stream()
                .map(post -> post.getComments().size())
                .collect(Collectors.toCollection(ArrayList::new));

        double maxLikes = calculatePercentile(likesCount);
        double maxComments = calculatePercentile(commentsCount);

        return new NormalizationValues(
                Math.max(maxLikes, 1.0),  // Nigdy nie dzielimy przez 0
                Math.max(maxComments, 1.0)
        );
    }

    private ScoredPost calculatePostScore(
            Post post,
            User currentUser,
            Date now,
            NormalizationValues normValues) {

        // 1. Oblicz podstawowy score za zaangażowanie
        double baseScore = calculateBaseEngagementScore(post, normValues);

        // 2. Oblicz bonus za świeżość
        double timeBoost = calculateTimeBoost(post.getCreatedDate(), now);

        // 3. Oblicz bonus społecznościowy
        double socialBoost = calculateSocialBoost(post, currentUser);

        // Połącz wszystkie komponenty z odpowiednimi wagami
        double finalScore = (baseScore * BASE_SCORE_WEIGHT) +
                (timeBoost * TIME_BOOST_MAX) +
                (socialBoost * SOCIAL_BOOST_MAX);

        return new ScoredPost(post, finalScore);
    }

    private double calculateBaseEngagementScore(Post post, NormalizationValues normValues) {
        double normalizedLikes = normalizeScore(post.getLikes().size(), normValues.getMaxLikes());
        double normalizedComments = normalizeScore(post.getComments().size(), normValues.getMaxComments());

        return (normalizedLikes * LIKE_WEIGHT) + (normalizedComments * COMMENT_WEIGHT);
    }

    private double calculateTimeBoost(Date postDate, Date now) {
        Duration age = Duration.between(postDate.toInstant(), now.toInstant());
        long hours = age.toHours();

        if (hours <= FRESH_HOURS) {
            return 1.0;  // Maksymalny boost dla świeżych postów
        } else if (hours <= RECENT_HOURS) {
            // Liniowy spadek między FRESH_HOURS a RECENT_HOURS
            return 1.0 - ((double)(hours - FRESH_HOURS) / (RECENT_HOURS - FRESH_HOURS));
        }
        return 0.0;  // Brak bonusu dla starszych postów
    }

    private double calculateSocialBoost(Post post, User currentUser) {
        if (currentUser.getFollowing().contains(post.getUserId())) {
            return 1.0;  // Pełny boost jeśli użytkownik obserwuje autora
        }
        return 0.0;
    }

    private double calculatePercentile(List<Integer> values) {
        if (values.isEmpty()) return 1.0;

        Collections.sort(values);
        int index = (int) Math.ceil(99 / 100.0 * values.size()) - 1;
        return Math.max(values.get(Math.max(index, 0)), 1.0);
    }

    private double normalizeScore(double value, double maxValue) {
        if (maxValue <= 0) return 0;
        return Math.min(value / maxValue, 1.0);
    }
}
