package com.dbms.flashcards.model;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Entity
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public Flashcard(String question, String answer, Difficulty diff, Topic topic) {
    }

    public Flashcard() {};

    public enum Difficulty { EASY, MEDIUM, HARD }

    // Getters & Setters
}