// FlashcardRepository.java
package com.dbms.flashcards.repository;

import com.dbms.flashcards.model.Flashcard;
import com.dbms.flashcards.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlashcardRepo extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByTopicId(Long topicId);
    // Find by difficulty
    List<Flashcard> findByDifficulty(Flashcard.Difficulty difficulty);

    // Find by both topicId and difficulty
    List<Flashcard> findByTopicIdAndDifficulty(Long topicId, Flashcard.Difficulty difficulty);

    // Shuffle cards (database-level random order)
    @Query(value = "SELECT * FROM flashcard ORDER BY RAND()", nativeQuery = true)
    List<Flashcard> findAllShuffled();

}

