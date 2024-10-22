package pl.jakubkonkol.tasteitserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubkonkol.tasteitserver.model.Tag;
import pl.jakubkonkol.tasteitserver.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag save(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null.");
        }
        return tagRepository.save(tag);
    }
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }
    public Optional<Tag> findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        return tagRepository.findByTagName(name);
    }
}
