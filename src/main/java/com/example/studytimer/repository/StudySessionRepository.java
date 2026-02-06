package com.example.studytimer.repository;

import com.example.studytimer.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySessionRepository
        extends JpaRepository<StudySession, Long> {
}

