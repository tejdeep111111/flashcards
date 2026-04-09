package com.dbms.flashcards.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
        this.question = question;
        this.answer = answer;
        this.difficulty = diff;
        this.topic = topic;
    }

    public Flashcard() {}

    public enum Difficulty { EASY, MEDIUM, HARD }
}