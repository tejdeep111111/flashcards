package com.dbms.flashcards.controller;

import com.dbms.flashcards.model.Topic;
import com.dbms.flashcards.service.FlashcardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlashcardApiController.class)
class FlashcardApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlashcardService service;

    @Test
    void editCard_returnsBadRequestWhenTopicIdIsInvalid() throws Exception {
        String body = """
                {
                  "topicId": "invalid",
                  "difficulty": "EASY",
                  "question": "Q",
                  "answer": "A"
                }
                """;

        mockMvc.perform(put("/api/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void editCard_returnsBadRequestWhenDifficultyIsInvalid() throws Exception {
        String body = """
                {
                  "topicId": "1",
                  "difficulty": "IMPOSSIBLE",
                  "question": "Q",
                  "answer": "A"
                }
                """;

        mockMvc.perform(put("/api/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void editCard_returnsBadRequestWhenRequiredFieldMissing() throws Exception {
        String body = """
                {
                  "topicId": "1",
                  "difficulty": "EASY",
                  "answer": "A"
                }
                """;

        mockMvc.perform(put("/api/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void editCard_returnsNotFoundWhenCardDoesNotExist() throws Exception {
        String body = """
                {
                  "topicId": "1",
                  "difficulty": "EASY",
                  "question": "Q",
                  "answer": "A"
                }
                """;

        Topic topic = new Topic();
        topic.setId(1L);
        when(service.getTopicById(1L)).thenReturn(topic);
        when(service.updateCard(eq(999L), any())).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Flashcard not found: 999")
        );

        mockMvc.perform(put("/api/cards/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }
}
