package com.dbms.flashcards.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String icon;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    private List<Flashcard> flashcards;

    // Getters & Setters
}