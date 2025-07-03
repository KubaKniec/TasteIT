package pl.jakubkonkol.tasteitserver.service;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.jakubkonkol.tasteitserver.dto.IngredientDto;
import pl.jakubkonkol.tasteitserver.dto.PageDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Ingredient;
import pl.jakubkonkol.tasteitserver.model.IngredientWrapper;
import pl.jakubkonkol.tasteitserver.model.projection.IngredientSearchView;
import pl.jakubkonkol.tasteitserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.service.interfaces.IIngredientService;
import org.springframework.cache.annotation.CacheEvict;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IngredientService implements IIngredientService {
    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    @Cacheable(value = "ingredients", key = "#name")
    public Optional<Ingredient> findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        return ingredientRepository.findByName(name);
    }

    @Cacheable(value = "ingredientsById", key = "#ingredientId")
    public IngredientDto getIngredient(String ingredientId) {
        var ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new ResourceNotFoundException("Ingredient not found with Id: " + ingredientId));
        return convertToDto(ingredient);
    }

    @CacheEvict(value = {"ingredients", "ingredientsById", "ingredientsPages", "ingredientsAll"}, allEntries = true)
    public IngredientDto save(IngredientDto ingredientDto) {
        Ingredient ingredient = convertToEntity(ingredientDto);
        Optional<Ingredient> existingIngredient = ingredientRepository.findByNameIgnoreCase(ingredient.getName());

        if (existingIngredient.isPresent()) {
            return convertToDto(existingIngredient.get());
        }

        ingredient = ingredientRepository.save(ingredient);
        return convertToDto(ingredient);
    }

    @CacheEvict(value = {"ingredients", "ingredientsById", "ingredientsPages", "ingredientsAll"}, allEntries = true)
    public List<IngredientDto> saveAll(List<IngredientDto> ingredientsDtos) {
        Set<String> existingNames = ingredientRepository
                .findByNameIgnoreCaseIn(
                        ingredientsDtos.stream()
                                .map(IngredientDto::getName)
                                .collect(Collectors.toSet())
                )
                .stream()
                .map(ingredient -> ingredient.getName().toLowerCase())
                .collect(Collectors.toSet());

        List<Ingredient> savedIngredients = ingredientRepository.saveAll(
                ingredientsDtos.stream()
                        .filter(dto -> !existingNames.contains(dto.getName().toLowerCase()))
                        .map(this::convertToEntity)
                        .collect(Collectors.toList())
        );

        return savedIngredients.stream()
                .map(this::convertToDto)
                .toList();
    }

    @CacheEvict(value = {"ingredients", "ingredientsById", "ingredientsPages", "ingredientsAll"}, allEntries = true)
    public List<IngredientDto> saveAllIngredients(List<Ingredient> ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("List of drinks cannot be null");
        }
        ingredients.forEach(ingredient -> {
            if (ingredient == null) {
                throw new IllegalArgumentException("Ingredient cannot be null");
            }
            if (ingredientRepository.findByNameIgnoreCase(ingredient.getName()).isEmpty()){
                ingredientRepository.save(ingredient);
            }
        });

        return ingredients.stream().map(this::convertToDto).toList();
    }

    @CacheEvict(value = {"ingredients", "ingredientsById", "ingredientsPages", "ingredientsAll"}, allEntries = true)
    public void deleteAll() {
        ingredientRepository.deleteAll();
    }

    @CacheEvict(value = {"ingredients", "ingredientsById", "ingredientsPages", "ingredientsAll"}, allEntries = true)
    public void deleteById(String ingredientId) {
        if (ingredientId == null || ingredientId.isEmpty()) {
            throw new IllegalArgumentException("Ingredient ID cannot be null or empty");
        }
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new IllegalStateException("Ingredient with ID " + ingredientId + " does not exist");
        }
        ingredientRepository.deleteById(ingredientId);
    }

    @Cacheable(value = "ingredientsAll", key = "'AllIngredients'")
    public List<IngredientDto> getAll() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Cacheable(value = "ingredientsPages", key = "'search_' + #name + '_page_' + #page + '_size_' + #size")
    public PageDto<IngredientDto> searchIngredientsByName(String name, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<IngredientSearchView> ingredients = ingredientRepository.findByNameContainingIgnoreCase(name, pageable);
        return getIngredientDtoFromIngredientSearchView(ingredients, pageable);
    }

    private static PageDto<IngredientDto> getIngredientDtoFromIngredientSearchView(Page<IngredientSearchView> ingredients, Pageable pageable) {
        List<IngredientDto> ingredientDtos = ingredients.getContent().stream().map(i -> {
            IngredientDto ingredientDto = new IngredientDto();
            ingredientDto.setIngredientId(i.getIngredientId());
            ingredientDto.setName(i.getName());
            return ingredientDto;
        }).toList();

        PageImpl<IngredientDto> pageImpl = new PageImpl<>(ingredientDtos, pageable, ingredients.getTotalElements());
        return getIngredientDtoPageDto(pageImpl);
    }

    private static PageDto<IngredientDto> getIngredientDtoPageDto(PageImpl<IngredientDto> pageImpl) {
        PageDto<IngredientDto> pageDto = new PageDto<>();
        pageDto.setContent(pageImpl.getContent());
        pageDto.setPageNumber(pageImpl.getNumber());
        pageDto.setPageSize(pageImpl.getSize());
        pageDto.setTotalElements(pageImpl.getTotalElements());
        pageDto.setTotalPages(pageImpl.getTotalPages());

        return pageDto;
    }

    public IngredientDto convertToDto(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDto.class);
    }

    public Ingredient convertToEntity(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, Ingredient.class);
    }

    public IngredientWrapper convertToWrapper(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientWrapper.class);
    }
}
