package com.dbms.flashcards.controller;

import com.dbms.flashcards.model.Flashcard;
import com.dbms.flashcards.model.Flashcard.Difficulty;
import com.dbms.flashcards.model.Topic;
import com.dbms.flashcards.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Long topicId     = Long.parseLong(body.get("topicId"));
        Difficulty diff  = Difficulty.valueOf(body.get("difficulty"));
        String question  = body.get("question");
        String answer    = body.get("answer");

        Topic topic = service.getTopicById(topicId);
        Flashcard card  = new Flashcard(question, answer, diff, topic);
        return ResponseEntity.ok(service.saveCard(card));
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        service.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}