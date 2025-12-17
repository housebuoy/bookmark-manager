package com.dev.bookmarkmanager.service;

import com.dev.bookmarkmanager.model.Tag;
import com.dev.bookmarkmanager.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getOrCreateTag(String name, String userId) {
        final String tagName = name.trim();

        return tagRepository.findByNameAndUserId(tagName, userId)
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    newTag.setUserId(userId); // <-- SET userId on the new tag
                    return tagRepository.save(newTag);
                });
    }

    public List<Tag> getAllTags(String userId) {
        return tagRepository.findAllByUserId(userId);
    };
}
