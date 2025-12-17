package com.dev.bookmarkmanager.controller;

import com.dev.bookmarkmanager.model.Bookmark;
import com.dev.bookmarkmanager.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dev.bookmarkmanager.dto.BookmarkRequest;

import java.security.Principal; // IMPT: Don't forget this import
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
@CrossOrigin(origins = "http://localhost:3000")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // Helper method to safely extract User ID
    private String getUserId(Principal principal) {
        return principal.getName();
    }

    @GetMapping
    public List<Bookmark> getAllBookmarks(Principal principal) {
        return bookmarkService.getAllBookmarks(getUserId(principal));
    }

    @GetMapping("/{id}")
    public Optional<Bookmark> getBookmarkById(@PathVariable String id, Principal principal) {
        return bookmarkService.getBookmarkById(id, getUserId(principal));
    }

    @PostMapping
    public Bookmark createBookmark(@RequestBody BookmarkRequest request, Principal principal) {
        Bookmark bookmark = Bookmark.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .url(request.getUrl())
                .favicon(request.getFavicon())
                .isPinned(request.isPinned())
                .isArchived(request.isArchived())
                .build();

        // PASS userId here
        return bookmarkService.saveBookmarkWithTags(bookmark, request.getTags(), getUserId(principal));
    }

    @PutMapping("/{id}")
    public Bookmark updateBookmark(
            @PathVariable UUID id,
            @RequestBody BookmarkRequest request,
            Principal principal
    ) {
        Bookmark bookmark = Bookmark.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .url(request.getUrl())
                .favicon(request.getFavicon())
                .isPinned(request.isPinned())
                .isArchived(request.isArchived())
                .build();

        // PASS userId here
        return bookmarkService.updateBookmarkWithTags(id, bookmark, request.getTags(), getUserId(principal));
    }

    @PatchMapping("/{id}/view")
    public ResponseEntity<Bookmark> markAsViewed(@PathVariable String id, Principal principal) {
        // PASS userId here
        Bookmark updated = bookmarkService.markAsViewed(id, getUserId(principal));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteBookmark(@PathVariable String id, Principal principal) {
        // PASS userId here
        bookmarkService.deleteBookmark(id, getUserId(principal));
    }

    @PatchMapping("/{id}/pin")
    public Bookmark togglePin(@PathVariable String id, Principal principal) {
        // PASS userId here
        return bookmarkService.togglePin(id, getUserId(principal));
    }

    @GetMapping("/pinned")
    public List<Bookmark> getPinnedBookmarks(Principal principal) {
        // PASS userId here
        return bookmarkService.getPinnedBookmarks(getUserId(principal));
    }

    @PatchMapping("/{id}/archive")
    public Bookmark toggleArchive(@PathVariable String id, Principal principal) {
        // PASS userId here
        return bookmarkService.toggleArchive(id, getUserId(principal));
    }

    @GetMapping("/by-tag")
    public List<Bookmark> getBookmarksByTag(@RequestParam String tag, Principal principal) {
        // PASS userId here
        return bookmarkService.getBookmarksByTag(tag, getUserId(principal));
    }
}