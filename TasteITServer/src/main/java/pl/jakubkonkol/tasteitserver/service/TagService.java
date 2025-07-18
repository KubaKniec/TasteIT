package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.data.BasicTagsData;
import pl.jakubkonkol.tasteitserver.dto.TagDto;
import pl.jakubkonkol.tasteitserver.dto.UserReturnDto;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.Tag;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.model.enums.TagType;
import pl.jakubkonkol.tasteitserver.model.projection.UserShort;
import pl.jakubkonkol.tasteitserver.repository.TagRepository;
import pl.jakubkonkol.tasteitserver.repository.UserRepository;
import pl.jakubkonkol.tasteitserver.service.interfaces.ITagService;
import pl.jakubkonkol.tasteitserver.service.interfaces.IUserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public Tag save(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null.");
        }
        return tagRepository.save(tag);
    }

//    @Cacheable(value = "allTags", key = "'allTags'")
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Optional<Tag> findByName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }

    @Cacheable(value = "tags", key = "#tagName")
    public List<TagDto> searchTagsByName(String tagName) {
        List<Tag> tags = tagRepository.findByTagNameContainingIgnoreCase(tagName);
        return tags.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Cacheable(value = "basicTags")
    public List<Tag> getBasicTags() {
        return tagRepository.findByTagType(TagType.BASIC);
    }

    @CacheEvict(value = {"tags", "basicTags", "'allTags'"}, allEntries = true)
    public void saveBasicTags(){
        for(String tagName: BasicTagsData.basicTags){
            Tag tag = new Tag();
            tag.setTagName(tagName);
            tag.setTagType(TagType.BASIC);
            tagRepository.save(tag);
        }
    }

    @CacheEvict(value = {"tags", "basicTags",  "'allTags'"}, allEntries = true)
    public void deleteAll(){
        tagRepository.deleteAll();
    }



    //    @CacheEvict(value = {"tags", "basicTags",  "'allTags'"}, allEntries = true)
    public void deleteById(String id,String sessionToken) {
        User user = userRepository.findBySessionToken(sessionToken).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getRoles().contains("ADMIN")) {
            throw new IllegalStateException("only admin can delete tags");
        }

        tagRepository.deleteById(id);
    }

    public TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    public Tag convertToEntity(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }
}
