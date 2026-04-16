package com.dbms.flashcards.service;

import com.dbms.flashcards.model.Flashcard;
import com.dbms.flashcards.model.Topic;
import com.dbms.flashcards.repository.FlashcardRepo;
import com.dbms.flashcards.repository.TopicRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.dbms.flashcards.model.Flashcard.Difficulty.EASY;
import static com.dbms.flashcards.model.Flashcard.Difficulty.HARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceTest {

    @Mock
    private FlashcardRepo flashcardRepo;

    @Mock
    private TopicRepo topicRepo;

    @InjectMocks
    private FlashcardService service;

    @Test
    void updateCard_updatesExistingCardAndSavesIt() {
        Topic oldTopic = new Topic();
        oldTopic.setId(1L);
        Topic newTopic = new Topic();
        newTopic.setId(2L);

        Flashcard existing = new Flashcard("Old question", "Old answer", EASY, oldTopic);
        existing.setId(10L);

        Flashcard update = new Flashcard("New question", "New answer", HARD, newTopic);

        when(flashcardRepo.findById(10L)).thenReturn(Optional.of(existing));
        when(flashcardRepo.save(existing)).thenReturn(existing);

        Flashcard result = service.updateCard(10L, update);

        assertSame(existing, result);
        assertEquals("New question", existing.getQuestion());
        assertEquals("New answer", existing.getAnswer());
        assertEquals(HARD, existing.getDifficulty());
        assertSame(newTopic, existing.getTopic());
        verify(flashcardRepo).save(existing);
    }

    @Test
    void updateCard_throwsNotFoundWhenCardDoesNotExist() {
        when(flashcardRepo.findById(77L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.updateCard(77L, new Flashcard())
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(flashcardRepo, never()).save(any(Flashcard.class));
    }
}
