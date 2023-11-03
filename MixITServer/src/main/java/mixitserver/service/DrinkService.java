package mixitserver.service;

import lombok.RequiredArgsConstructor;
import mixitserver.model.dto.DrinkDTO;
import mixitserver.repository.DrinkRepository;
import mixitserver.service.mapper.DrinkMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrinkService {
    private final DrinkRepository drinkRepository;
    private final DrinkMapper drinkMapper;

    public DrinkDTO save(DrinkDTO drinkDTO) {
        return DrinkMapper.getInstace().mapToDto(
                drinkRepository.save(DrinkMapper.getInstace().mapToDomain(drinkDTO))
        );
    }

}
