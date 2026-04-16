// FlashcardRepository.java
package com.dbms.flashcards.repository;

import com.dbms.flashcards.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlashcardRepo extends JpaRepository<Flashcard, Long> {

    // Find by topicId
    @Query("SELECT f FROM Flashcard f WHERE f.topic.id = :topicId")
    List<Flashcard> findByTopicId(@Param("topicId") Long topicId);

    // Find by difficulty
    @Query("SELECT f FROM Flashcard f WHERE f.difficulty = :difficulty")
    List<Flashcard> findByDifficulty(@Param("difficulty") Flashcard.Difficulty difficulty);

    // Find by both topicId and difficulty
    @Query("SELECT f FROM Flashcard f WHERE f.topic.id = :topicId AND f.difficulty = :difficulty")
    List<Flashcard> findByTopicIdAndDifficulty(@Param("topicId") Long topicId,
                                               @Param("difficulty") Flashcard.Difficulty difficulty);

    // Shuffle cards (database-level random order)
    @Query(value = "SELECT * FROM flashcard ORDER BY RAND()", nativeQuery = true)
    List<Flashcard> findAllShuffled();
}

