package com.swmaestro.backend.service;

import com.swmaestro.backend.dto.SendEmailRequest;
import com.swmaestro.backend.dto.SendEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public SendEmailResponse sendCurriculum(SendEmailRequest req) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(req.email());
        message.setSubject("[SW Maestro] 나만의 학습 커리큘럼이 도착했습니다");
        message.setText(req.markdown());

        mailSender.send(message);

        return new SendEmailResponse("sent");
    }
}
