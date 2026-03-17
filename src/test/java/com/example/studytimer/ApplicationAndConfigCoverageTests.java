package com.example.studytimer;

import com.example.studytimer.config.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class ApplicationAndConfigCoverageTests {

    @Test
    void mainDelegatesToSpringApplicationRun() {
        String[] args = {"--spring.main.web-application-type=none"};

        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            StudytimerApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(StudytimerApplication.class, args));
        }
    }

    @Test
    void globalExceptionHandlerReturnsInternalServerErrorPayload() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<Map<String, String>> response =
                handler.handleAllExceptions(new RuntimeException("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An internal server error occurred", response.getBody().get("error"));
    }
}