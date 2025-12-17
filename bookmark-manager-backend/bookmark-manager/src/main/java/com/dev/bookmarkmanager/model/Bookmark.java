package com.dev.bookmarkmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import lombok.*;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "\"bookmark\"")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    @Column (columnDefinition = "Text")
    private String description;
    private String url;
    private String favicon;
    @Column(name = "is_pinned")
    @JsonProperty("isPinned")
    private boolean isPinned;
    @Column(name = "is_archived")
    private boolean isArchived;
    @Column(name = "view_count")
    @JsonProperty("viewCount")
    private int viewCount;
    @Column(name = "last_visited")
    @JsonProperty("lastVisited")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastVisited;
    @Column(name = "date_added")
    private LocalDateTime dateAdded;
    @Column(name = "user_id")
    private String userId;

    @PrePersist
    protected void onCreate() {
        this.dateAdded = LocalDateTime.now();
    }

    @ManyToMany
    @JoinTable(
            name = "\"bookmark_tags\"",
            joinColumns = @JoinColumn(name="\"bookmark_id\""),
            inverseJoinColumns = @JoinColumn(name = "\"tag_id\"")

    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("bookmarks")
    private Set<Tag> tags = new HashSet<>();
}
