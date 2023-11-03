package mixitserver.service.mapper;

import mixitserver.model.domain.Ingredient;
import mixitserver.model.dto.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface IngredientMapper {
    static IngredientMapper getInstace() {
        return Mappers.getMapper(IngredientMapper.class);
    }

    IngredientDTO mapToDto(Ingredient ingredient);
    Ingredient mapToDomain(IngredientDTO ingredientDTO);
}
