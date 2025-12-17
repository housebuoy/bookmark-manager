package com.dev.bookmarkmanager.repository;

import com.dev.bookmarkmanager.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.name = :name AND t.userId = :userId")
    Optional<Tag> findByNameAndUserId(
            @Param("name") String name,
            @Param("userId") String userId
    );

    @Query("SELECT t FROM Tag t WHERE t.userId = :userId")
    List<Tag> findAllByUserId(@Param("userId") String userId);
}
