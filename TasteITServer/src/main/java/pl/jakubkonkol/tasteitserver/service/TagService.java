package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.dto.TagDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.model.Tag;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.repository.TagRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public Tag save(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null.");
        }
        return tagRepository.save(tag);
    }
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Optional<Tag> findByName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }

    public List<TagDto> searchTagsByName(String tagName) {
        List<Tag> tags = tagRepository.findByTagNameContainingIgnoreCase(tagName);
        return tags.stream()
                .map(this::convertToDto)
                .toList();
    }

    private TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }
}
