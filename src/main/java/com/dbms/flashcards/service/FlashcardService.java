package com.dbms.flashcards.service;

import com.dbms.flashcards.model.Flashcard;
import com.dbms.flashcards.model.Flashcard.Difficulty;
import com.dbms.flashcards.model.Topic;
import com.dbms.flashcards.repository.FlashcardRepo;
import com.dbms.flashcards.repository.TopicRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FlashcardService {

    private final FlashcardRepo flashcardRepo;
    private final TopicRepo topicRepo;

    public FlashcardService(FlashcardRepo flashcardRepo, TopicRepo topicRepo) {
        this.flashcardRepo = flashcardRepo;
        this.topicRepo = topicRepo;
    }

    // ── Topics ────────────────────────────────────────────────────────────

    // Called by GET /api/topics
    public List<Topic> getAllTopics() {
        return topicRepo.findAll();
    }

    // Called by POST /api/cards (resolves topicId -> Topic entity)
    public Topic getTopicById(Long id) {
        return topicRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found: " + id));
    }

    // ── Flashcards ────────────────────────────────────────────────────────

    // Called by GET /api/cards?shuffle=true
    public List<Flashcard> getShuffledCards() {
        return flashcardRepo.findAllShuffled();
    }

    // Called by GET /api/cards?topicId=&difficulty= (both params optional)
    public List<Flashcard> getCardsByTopicAndDifficulty(Long topicId, Flashcard.Difficulty difficulty) {
        return flashcardRepo.findByTopicIdAndDifficulty(topicId, difficulty);
    }

    // Called by POST /api/cards
    @Transactional
    public Flashcard saveCard(Flashcard card) {
        return flashcardRepo.save(card);
    }

    // Called by DELETE /api/cards/{id}
    @Transactional
    public void deleteCard(Long id) {
        if (!flashcardRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Flashcard not found: " + id);
        }
        flashcardRepo.deleteById(id);
    }
}