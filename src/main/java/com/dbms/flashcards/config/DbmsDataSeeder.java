package com.dbms.flashcards.config;

import com.dbms.flashcards.model.Flashcard;
import com.dbms.flashcards.model.Topic;
import com.dbms.flashcards.repository.FlashcardRepo;
import com.dbms.flashcards.repository.TopicRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DbmsDataSeeder implements CommandLineRunner {

    private final TopicRepo topicRepo;
    private final FlashcardRepo flashcardRepo;

    public DbmsDataSeeder(TopicRepo topicRepo, FlashcardRepo flashcardRepo) {
        this.topicRepo = topicRepo;
        this.flashcardRepo = flashcardRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        List<Topic> existingTopics = topicRepo.findAll();
        Map<String, Topic> topicsByName = new HashMap<>();
        for (Topic topic : existingTopics) {
            topicsByName.put(topic.getName(), topic);
        }

        Set<String> existingQuestions = new HashSet<>();
        for (Flashcard card : flashcardRepo.findAll()) {
            existingQuestions.add(normalize(card.getQuestion()));
        }

        List<Flashcard> cardsToInsert = new ArrayList<>();
        for (CardSeed seed : seeds()) {
            Topic topic = topicsByName.computeIfAbsent(seed.topicName, name -> {
                Topic t = new Topic();
                t.setName(seed.topicName);
                t.setIcon(seed.topicIcon);
                return topicRepo.save(t);
            });

            String normalizedQuestion = normalize(seed.question);
            if (!existingQuestions.contains(normalizedQuestion)) {
                cardsToInsert.add(new Flashcard(seed.question, seed.answer, seed.difficulty, topic));
                existingQuestions.add(normalizedQuestion);
            }
        }

        if (!cardsToInsert.isEmpty()) {
            flashcardRepo.saveAll(cardsToInsert);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private List<CardSeed> seeds() {
        return List.of(
                new CardSeed("SQL Basics", "🧾", Flashcard.Difficulty.EASY,
                        "What is a primary key in a table?",
                        "A primary key uniquely identifies each row in a table and cannot contain NULL values."),
                new CardSeed("SQL Basics", "🧾", Flashcard.Difficulty.EASY,
                        "What is a foreign key?",
                        "A foreign key is a column that references the primary key of another table to enforce relationships."),
                new CardSeed("Transactions", "💾", Flashcard.Difficulty.MEDIUM,
                        "What does ACID stand for in DBMS?",
                        "Atomicity, Consistency, Isolation, and Durability."),
                new CardSeed("Transactions", "💾", Flashcard.Difficulty.MEDIUM,
                        "What is meant by atomicity?",
                        "A transaction is treated as an indivisible unit: either all operations succeed or all fail."),
                new CardSeed("Normalization", "📐", Flashcard.Difficulty.MEDIUM,
                        "What is the goal of normalization?",
                        "To reduce data redundancy and improve data integrity by organizing data into well-structured tables."),
                new CardSeed("Normalization", "📐", Flashcard.Difficulty.HARD,
                        "What is Third Normal Form (3NF)?",
                        "A table is in 3NF when it is in 2NF and non-key attributes are not transitively dependent on the primary key."),
                new CardSeed("Indexing", "🗂️", Flashcard.Difficulty.MEDIUM,
                        "Why are indexes used in databases?",
                        "Indexes improve query performance by allowing faster data retrieval, especially for search and join operations."),
                new CardSeed("ER Modeling", "🧩", Flashcard.Difficulty.EASY,
                        "What is an entity in an ER model?",
                        "An entity is a real-world object or concept that can be uniquely identified and stored in a database."),
                new CardSeed("ER Modeling", "🧩", Flashcard.Difficulty.EASY,
                        "What is the difference between one-to-many and many-to-many relationships?",
                        "One-to-many links one record to many records in another table; many-to-many requires a junction table to link both sides."),
                new CardSeed("Concurrency", "🔒", Flashcard.Difficulty.HARD,
                        "What is a deadlock in DBMS?",
                        "A deadlock occurs when two or more transactions wait indefinitely for resources locked by each other.")
        );
    }

    private record CardSeed(
            String topicName,
            String topicIcon,
            Flashcard.Difficulty difficulty,
            String question,
            String answer
    ) {}
}
