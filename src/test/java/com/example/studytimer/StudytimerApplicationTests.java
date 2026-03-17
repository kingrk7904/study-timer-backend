package com.example.studytimer;

import com.example.studytimer.controller.StudySessionController;
import com.example.studytimer.model.StudySession;
import com.example.studytimer.repository.StudySessionRepository;
import com.example.studytimer.service.StudySessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudytimerApplicationTests {

    @Autowired
    private StudySessionRepository repository;

    @Autowired
    private StudySessionService service;

    @Autowired
    private StudySessionController controller;

    private StudySession testSession;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        testSession = new StudySession("Mathematics", 60);
    }

    // ============ APPLICATION STARTUP TESTS ============
    @Test
    void contextLoads() {
        assertNotNull(controller);
        assertNotNull(service);
        assertNotNull(repository);
    }

    @Test
    void testApplicationStartup() {
        assertNotNull(service);
        assertNotNull(repository);
        assertNotNull(controller);
    }

    // ============ STUDYSESSION MODEL TESTS ============
    @Test
    void testStudySessionNoArgConstructor() {
        StudySession session = new StudySession();
        assertNull(session.getId());
        assertNull(session.getSubject());
        assertEquals(0, session.getDuration());
    }

    @Test
    void testStudySessionConstructorWithArgs() {
        StudySession session = new StudySession("Physics", 90);
        assertEquals("Physics", session.getSubject());
        assertEquals(90, session.getDuration());
    }

    @Test
    void testStudySessionSetSubject() {
        StudySession session = new StudySession();
        session.setSubject("Chemistry");
        assertEquals("Chemistry", session.getSubject());
    }

    @Test
    void testStudySessionSetDuration() {
        StudySession session = new StudySession();
        session.setDuration(45);
        assertEquals(45, session.getDuration());
    }

    @Test
    void testStudySessionGetId() {
        StudySession session = new StudySession("English", 30);
        assertNull(session.getId());
    }

    @Test
    void testStudySessionMultipleOperations() {
        StudySession session = new StudySession("Art", 120);
        session.setSubject("Science");
        session.setDuration(90);
        assertEquals("Science", session.getSubject());
        assertEquals(90, session.getDuration());
    }

    // ============ SERVICE TESTS ============
    @Test
    void testSaveSession() {
        StudySession saved = service.saveSession(testSession);
        assertNotNull(saved.getId());
        assertEquals("Mathematics", saved.getSubject());
        assertEquals(60, saved.getDuration());
    }

    @Test
    void testGetAllSessions() {
        service.saveSession(new StudySession("Math", 60));
        service.saveSession(new StudySession("Science", 45));
        var sessions = service.getAllSessions();
        assertEquals(2, sessions.size());
    }

    @Test
    void testGetAllSessionsEmpty() {
        var sessions = service.getAllSessions();
        assertEquals(0, sessions.size());
    }

    @Test
    void testGetSessionById() {
        StudySession saved = service.saveSession(testSession);
        Optional<StudySession> retrieved = service.getSessionById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Mathematics", retrieved.get().getSubject());
    }

    @Test
    void testGetSessionByIdNotFound() {
        Optional<StudySession> retrieved = service.getSessionById(999L);
        assertFalse(retrieved.isPresent());
    }

    @Test
    void testUpdateSession() {
        StudySession saved = service.saveSession(testSession);
        StudySession updated = new StudySession("Physics", 75);
        Optional<StudySession> result = service.updateSession(saved.getId(), updated);
        assertTrue(result.isPresent());
        assertEquals("Physics", result.get().getSubject());
        assertEquals(75, result.get().getDuration());
    }

    @Test
    void testUpdateSessionNotFound() {
        StudySession updated = new StudySession("Physics", 75);
        Optional<StudySession> result = service.updateSession(999L, updated);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteSession() {
        StudySession saved = service.saveSession(testSession);
        service.deleteSession(saved.getId());
        Optional<StudySession> retrieved = service.getSessionById(saved.getId());
        assertFalse(retrieved.isPresent());
    }

    @Test
    void testDeleteNonExistentSession() {
        service.deleteSession(999L);
        // Should not throw, just silently succeed
        assertTrue(true);
    }

    // ============ CONTROLLER TESTS ============
    @Test
    void testControllerGetAllSessions() {
        service.saveSession(new StudySession("Math", 60));
        service.saveSession(new StudySession("Science", 45));
        var result = controller.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void testControllerGetSessionById() {
        StudySession saved = service.saveSession(testSession);
        var result = controller.getById(saved.getId());
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertNotNull(result.getBody());
        assertEquals("Mathematics", result.getBody().getSubject());
    }

    @Test
    void testControllerGetSessionByIdNotFound() {
        var result = controller.getById(999L);
        assertTrue(result.getStatusCode().is4xxClientError());
    }

    @Test
    void testControllerCreateSession() {
        StudySession session = new StudySession("Python", 120);
        var result = controller.create(session);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getId());
    }

    @Test
    void testControllerUpdateSession() {
        StudySession saved = service.saveSession(testSession);
        StudySession updated = new StudySession("Chemistry", 100);
        var result = controller.update(saved.getId(), updated);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertNotNull(result.getBody());
        assertEquals("Chemistry", result.getBody().getSubject());
    }

    @Test
    void testControllerUpdateSessionNotFound() {
        StudySession updated = new StudySession("Biology", 50);
        var result = controller.update(999L, updated);
        assertTrue(result.getStatusCode().is4xxClientError());
    }

    @Test
    void testControllerDeleteSession() {
        StudySession saved = service.saveSession(testSession);
        var result = controller.delete(saved.getId());
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testControllerDeleteSessionNotFound() {
        var result = controller.delete(999L);
        assertTrue(result.getStatusCode().is4xxClientError());
    }

    @Test
    void testControllerRoot() {
        String result = controller.root();
        assertEquals("Backend is running", result);
    }

    // ============ INTEGRATION & WORKFLOW TESTS ============
    @Test
    void testFullCRUDWorkflow() {
        // Create
        StudySession session = new StudySession("Integrated", 75);
        StudySession created = service.saveSession(session);
        assertNotNull(created.getId());

        // Read
        Optional<StudySession> read = service.getSessionById(created.getId());
        assertTrue(read.isPresent());
        assertEquals("Integrated", read.get().getSubject());

        // Update
        StudySession updateData = new StudySession("UpdatedIntegrated", 100);
        Optional<StudySession> updated = service.updateSession(created.getId(), updateData);
        assertTrue(updated.isPresent());
        assertEquals("UpdatedIntegrated", updated.get().getSubject());

        // Delete
        service.deleteSession(created.getId());
        Optional<StudySession> deleted = service.getSessionById(created.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void testMultipleSessionOperations() {
        StudySession session1 = service.saveSession(new StudySession("Math", 60));
        StudySession session2 = service.saveSession(new StudySession("Science", 45));
        StudySession session3 = service.saveSession(new StudySession("History", 30));

        assertEquals(3, service.getAllSessions().size());

        service.deleteSession(session2.getId());
        assertEquals(2, service.getAllSessions().size());

        service.deleteSession(session1.getId());
        service.deleteSession(session3.getId());
        assertEquals(0, service.getAllSessions().size());
    }

    @Test
    void testSessionPersistence() {
        StudySession session = new StudySession("Persistence Test", 55);
        StudySession saved = service.saveSession(session);
        
        Optional<StudySession> retrieved = service.getSessionById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(saved.getId(), retrieved.get().getId());
        assertEquals("Persistence Test", retrieved.get().getSubject());
        assertEquals(55, retrieved.get().getDuration());
    }

    @Test
    void testMultipleUpdates() {
        StudySession session = service.saveSession(new StudySession("Multi", 30));
        
        service.updateSession(session.getId(), new StudySession("Multi1", 40));
        service.updateSession(session.getId(), new StudySession("Multi2", 50));
        service.updateSession(session.getId(), new StudySession("Multi3", 60));
        
        Optional<StudySession> final_result = service.getSessionById(session.getId());
        assertTrue(final_result.isPresent());
        assertEquals("Multi3", final_result.get().getSubject());
        assertEquals(60, final_result.get().getDuration());
    }
}
