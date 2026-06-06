package com.swmaestro.backend.service;

import com.swmaestro.backend.dto.SendEmailRequest;
import com.swmaestro.backend.dto.SendEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public SendEmailResponse sendCurriculum(SendEmailRequest req) {
        log.info("이메일 전송 시도: to={}", req.email());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(req.email());
            message.setSubject("[SW Maestro] 나만의 학습 커리큘럼이 도착했습니다");
            message.setText(req.markdown());

            mailSender.send(message);
            log.info("이메일 전송 성공: to={}", req.email());
        } catch (Exception e) {
            log.error("이메일 전송 실패: {}", e.toString(), e);
            throw e;
        }

        return new SendEmailResponse("sent");
    }
}
