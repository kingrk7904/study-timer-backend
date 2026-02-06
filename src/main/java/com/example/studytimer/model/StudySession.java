package com.example.studytimer.model;

import jakarta.persistence.*;

@Entity
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private int duration; // minutes studied

    public StudySession() {}

    public StudySession(String subject, int duration) {
        this.subject = subject;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public int getDuration() {
        return duration;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

