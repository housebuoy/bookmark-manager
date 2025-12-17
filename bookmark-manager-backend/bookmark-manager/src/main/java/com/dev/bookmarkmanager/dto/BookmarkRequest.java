package com.dev.bookmarkmanager.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookmarkRequest {
    private String title;
    private String description;
    private String url;
    private String favicon;
    private boolean isPinned;
    private boolean isArchived;
    private List<String> tags;
}
