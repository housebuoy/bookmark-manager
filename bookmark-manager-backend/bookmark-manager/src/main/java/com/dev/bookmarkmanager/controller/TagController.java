package com.dev.bookmarkmanager.controller;

import com.dev.bookmarkmanager.model.Tag;
import com.dev.bookmarkmanager.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal; // IMPT: Add this import
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // Create or fetch a tag (User Scoped)
    @PostMapping
    public Tag createOrGetTag(@RequestParam String name, Principal principal) {
        // Pass the user ID to the service
        return tagService.getOrCreateTag(name, principal.getName());
    }

    // Get all tags (User Scoped)
    @GetMapping
    public List<Tag> getAllTags(Principal principal) {
        // Pass the user ID to the service
        return tagService.getAllTags(principal.getName());
    }
}