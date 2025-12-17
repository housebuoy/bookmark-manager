package com.dev.bookmarkmanager.service;

import com.dev.bookmarkmanager.model.Bookmark;
import com.dev.bookmarkmanager.model.Tag;
import com.dev.bookmarkmanager.repository.BookmarkRepository;
import com.dev.bookmarkmanager.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;

    public BookmarkService(BookmarkRepository bookmarkRepository, TagRepository tagRepository, TagService tagService) {
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
        this.tagService = tagService;
    }

    @Transactional
    public Bookmark updateBookmarkWithTags(UUID id, Bookmark updatedBookmark, List<String> tagNames, String userId) {
        // Enforce ownership check
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found or access denied"));

        // Use user-scoped tag creation
        Set<Tag> tags = tagNames.stream()
                .map(tagName -> tagService.getOrCreateTag(tagName, userId))
                .collect(Collectors.toSet());

        // ... update fields (title, url, etc.) ...
        bookmark.setTitle(updatedBookmark.getTitle());
        bookmark.setUrl(updatedBookmark.getUrl());
        bookmark.setDescription(updatedBookmark.getDescription());
        bookmark.setFavicon(updatedBookmark.getFavicon());
        bookmark.setPinned(updatedBookmark.isPinned());
        bookmark.setArchived(updatedBookmark.isArchived());
        bookmark.setTags(tags);
        // bookmark.userId is already correct from the fetched bookmark

        return bookmarkRepository.save(bookmark);
    }

    // Toggle bookmark pin status
    public Bookmark togglePin(String id, String userId) {
        // FIX: Pass userId to repository and removed extra '('
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(UUID.fromString(id), userId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        bookmark.setPinned(!bookmark.isPinned());
        return bookmarkRepository.save(bookmark);
    }

    // Toggle archive status
    public Bookmark toggleArchive(String id, String userId) {
        // FIX: Pass userId to repository and removed extra '('
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(UUID.fromString(id), userId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        bookmark.setArchived(!bookmark.isArchived());

        if (bookmark.isArchived()) {
            bookmark.setPinned(false);
        }

        return bookmarkRepository.save(bookmark);
    }

    // Increase view count & update last visited date
    public Bookmark markAsViewed(String id, String userId) {
        // FIX: Pass userId to repository and removed extra '('
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(UUID.fromString(id), userId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        bookmark.setViewCount(bookmark.getViewCount() + 1);
        bookmark.setLastVisited(LocalDateTime.now());

        return bookmarkRepository.save(bookmark);
    }

    // Get all bookmarks
    public List<Bookmark> getAllBookmarks(String userId) {
        return bookmarkRepository.findAllByUserId(userId);
    }

    // Get one by ID
    public Optional<Bookmark> getBookmarkById(String id, String userId) {
        return bookmarkRepository.findByIdAndUserId(UUID.fromString(id), userId);
    }

    // Save / Create bookmark
    public Bookmark saveBookmark(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    // Delete bookmark
    public void deleteBookmark(String id, String userId) {
        // Enforce ownership check
        Bookmark bookmark = bookmarkRepository.findByIdAndUserId(UUID.fromString(id), userId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found or access denied"));

        bookmarkRepository.delete(bookmark);
    }

    public Bookmark saveBookmarkWithTags(Bookmark bookmark, List<String> tagNames, String userId) {

        bookmark.setUserId(userId); // <-- SET userId on the new bookmark

        Set<Tag> tags = tagNames.stream()
                .map(tagName -> tagService.getOrCreateTag(tagName, userId))
                .collect(Collectors.toSet());

        bookmark.setTags(tags);

        // Set default values if they are missing
        if (bookmark.getDateAdded() == null) {
            bookmark.setDateAdded(LocalDateTime.now());
        }
        bookmark.setViewCount(0);

        return bookmarkRepository.save(bookmark);
    }

    public List<Bookmark> getBookmarksByTag(String tag, String userId) {
        // FIX: Use the user-scoped repository method
        return bookmarkRepository.findByTagsNameIgnoreCaseAndUserId(tag, userId);
    }

    public List<Bookmark> getPinnedBookmarks(String userId) {
        // FIX: Use the repository method for efficiency and security
        return bookmarkRepository.findAllByIsPinnedTrueAndUserId(userId);
    }
}