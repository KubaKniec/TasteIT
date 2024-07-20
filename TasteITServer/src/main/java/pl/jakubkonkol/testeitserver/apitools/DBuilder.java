package pl.jakubkonkol.testeitserver.apitools;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBuilder {
    private final FoodFetcher foodFetcher;
    @PostConstruct
    public void buildDataBase(){
        foodFetcher.populateDBWithFood();
    }
}
