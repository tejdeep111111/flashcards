package com.dbms.flashcards.controller;

import com.dbms.flashcards.model.Flashcard;
import com.dbms.flashcards.model.Flashcard.Difficulty;
import com.dbms.flashcards.model.Topic;
import com.dbms.flashcards.service.FlashcardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FlashcardApiController {

    private final FlashcardService service;

    public FlashcardApiController(FlashcardService service) {
        this.service = service;
    }

    // ── Topics ────────────────────────────────────────────────────────────

    @GetMapping("/topics")
    public List<Topic> getAllTopics() {
        return service.getAllTopics();
    }

    // ── Flashcards ────────────────────────────────────────────────────────

    @GetMapping("/cards")
    public List<Flashcard> getCards(
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(defaultValue = "false") boolean shuffle) {

        if (shuffle) return service.getShuffledCards();
        return service.getCardsByTopicAndDifficulty(topicId, difficulty);
    }

    @PostMapping("/cards")
    public ResponseEntity<Flashcard> addCard(@RequestBody Map<String, String> body) {
        Long topicId = parseTopicId(body);
        Difficulty diff = parseDifficulty(body);
        String question = requireField(body, "question");
        String answer = requireField(body, "answer");

        Topic topic = service.getTopicById(topicId);
        Flashcard card = new Flashcard(question, answer, diff, topic);
        return ResponseEntity.ok(service.saveCard(card));
    }

    @PutMapping("/cards/{id}")
    public ResponseEntity<Flashcard> editCard(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Long topicId = parseTopicId(body);
        Difficulty diff = parseDifficulty(body);
        String question = requireField(body, "question");
        String answer = requireField(body, "answer");

        Topic topic = service.getTopicById(topicId);
        Flashcard card = new Flashcard(question, answer, diff, topic);
        return ResponseEntity.ok(service.updateCard(id, card));
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        service.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    private String requireField(Map<String, String> body, String fieldName) {
        String value = body.get(fieldName);
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " is required");
        }
        return value;
    }

    private Long parseTopicId(Map<String, String> body) {
        String value = requireField(body, "topicId");
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topicId");
        }
    }

    private Difficulty parseDifficulty(Map<String, String> body) {
        String value = requireField(body, "difficulty");
        try {
            return Difficulty.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid difficulty");
        }
    }
}
