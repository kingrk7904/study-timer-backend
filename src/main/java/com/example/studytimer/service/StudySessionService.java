package com.example.studytimer.service;

import com.example.studytimer.model.StudySession;
import com.example.studytimer.repository.StudySessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudySessionService {

    private final StudySessionRepository repository;

    public StudySessionService(StudySessionRepository repository) {
        this.repository = repository;
    }

    public List<StudySession> getAllSessions() {
        return repository.findAll();
    }

    public Optional<StudySession> getSessionById(Long id) {
        return repository.findById(id);
    }

    public StudySession saveSession(StudySession session) {
        return repository.save(session);
    }

    public Optional<StudySession> updateSession(Long id, StudySession updatedData) {
        return repository.findById(id).map(existing -> {
            existing.setSubject(updatedData.getSubject());
            existing.setDuration(updatedData.getDuration());
            return repository.save(existing);
        });
    }

    public void deleteSession(Long id) {
        repository.deleteById(id);
    }
}

