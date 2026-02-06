package com.example.studytimer.controller;

import com.example.studytimer.model.StudySession;
import com.example.studytimer.service.StudySessionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "http://localhost:3000")
public class StudySessionController {

    private final StudySessionService service;

    public StudySessionController(StudySessionService service) {
        this.service = service;
    }

    @GetMapping
    public List<StudySession> getAll() {
        return service.getAllSessions();
    }

    @PostMapping
    public StudySession add(@RequestBody StudySession session) {
        return service.saveSession(session);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteSession(id);
    }
}
