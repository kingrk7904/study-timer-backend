package com.example.studytimer.service;

import com.example.studytimer.model.StudySession;
import com.example.studytimer.repository.StudySessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class StudySessionService {

    private final StudySessionRepository repository;

    public StudySessionService(StudySessionRepository repository) {
        this.repository = repository;
    }

    public List<StudySession> getAllSessions() {
        return repository.findAll();
    }

    public StudySession saveSession(StudySession session) {
        return repository.save(session);
    }

    public void deleteSession(Long id) {
        repository.deleteById(id);
    }
}

