package com.swmaestro.backend.controller;

import com.swmaestro.backend.dto.*;
import com.swmaestro.backend.service.CurriculumService;
import com.swmaestro.backend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurriculumController {

    private static final Logger log = LoggerFactory.getLogger(CurriculumController.class);

    private final CurriculumService curriculumService;
    private final EmailService emailService;

    public CurriculumController(CurriculumService curriculumService, EmailService emailService) {
        this.curriculumService = curriculumService;
        this.emailService      = emailService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerateResponse> generate(@RequestBody GenerateRequest req) {
        return ResponseEntity.ok(curriculumService.generateQuestions(req));
    }

    @PostMapping("/build")
    public ResponseEntity<BuildResponse> build(@RequestBody BuildRequest req) {
        return ResponseEntity.ok(curriculumService.buildCurriculum(req));
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest req) {
        return ResponseEntity.ok(curriculumService.chat(req));
    }

    @PostMapping("/send-email")
    public ResponseEntity<SendEmailResponse> sendEmail(@RequestBody SendEmailRequest req) {
        return ResponseEntity.ok(emailService.sendCurriculum(req));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidation(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception e) {
        log.error("Unhandled exception", e);
        return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
    }
}
