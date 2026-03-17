package com.example.studytimer.controller;

import com.example.studytimer.model.StudySession;
import com.example.studytimer.service.StudySessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudySessionController {

    private final StudySessionService service;

    public StudySessionController(StudySessionService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String root() {
        return "Backend is running";
    }

    @GetMapping("/api/sessions")
    public List<StudySession> getAll() {
        return service.getAllSessions();
    }

    @GetMapping("/api/sessions/{id}")
    public ResponseEntity<StudySession> getById(@PathVariable Long id) {
        return service.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/sessions")
    public ResponseEntity<StudySession> create(@RequestBody StudySession session) {
        StudySession saved = service.saveSession(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/api/sessions/{id}")
    public ResponseEntity<StudySession> update(@PathVariable Long id, @RequestBody StudySession session) {
        return service.updateSession(id, session)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/sessions/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.getSessionById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
