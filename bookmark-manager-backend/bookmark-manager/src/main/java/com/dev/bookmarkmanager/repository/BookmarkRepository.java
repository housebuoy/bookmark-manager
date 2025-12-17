package com.dev.bookmarkmanager.repository;

import com.dev.bookmarkmanager.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {

    @Query("""
           SELECT b FROM Bookmark b 
           JOIN b.tags t 
           WHERE LOWER(t.name) = LOWER(:tagName) 
           AND b.userId = :userId
           """)
    List<Bookmark> findByTagsNameIgnoreCaseAndUserId(
            @Param("tagName") String tagName,
            @Param("userId") String userId
    );

    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId")
    List<Bookmark> findAllByUserId(@Param("userId") String userId);

    @Query("SELECT b FROM Bookmark b WHERE b.id = :id AND b.userId = :userId")
    Optional<Bookmark> findByIdAndUserId(
            @Param("id") UUID id,
            @Param("userId") String userId
    );

    @Query("SELECT b FROM Bookmark b WHERE b.isPinned = true AND b.userId = :userId")
    List<Bookmark> findAllByIsPinnedTrueAndUserId(@Param("userId") String userId);
}
