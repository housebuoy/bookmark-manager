package com.dev.bookmarkmanager.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Entity
@Table(name = "\"tag\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "user_id")
    private String userId;

    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Bookmark> bookmarks = new HashSet<>();

    public Set<Bookmark> getBookmarks() {
        if (bookmarks == null) {
            bookmarks = new HashSet<>();
        }
        return bookmarks;
    }
}
