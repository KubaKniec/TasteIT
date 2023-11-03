package mixitserver.service.mapper;

import mixitserver.model.domain.Drink;
import mixitserver.model.dto.DrinkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DrinkMapper {
    static DrinkMapper getInstace() {
        return Mappers.getMapper(DrinkMapper.class);
    }

    DrinkDTO mapToDto(Drink drink);
    Drink mapToDomain(DrinkDTO drinkDTO);
}
