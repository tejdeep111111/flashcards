package com.dbms.flashcards.repository;

import com.dbms.flashcards.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepo extends JpaRepository<Topic, Long> {}