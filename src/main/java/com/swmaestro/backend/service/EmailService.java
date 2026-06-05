package com.swmaestro.backend.service;

import com.swmaestro.backend.dto.SendEmailRequest;
import com.swmaestro.backend.dto.SendEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final String RESEND_URL = "https://api.resend.com/emails";

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from:onboarding@resend.dev}")
    private String fromAddress;

    private final RestTemplate restTemplate;

    public EmailService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5_000);
        factory.setReadTimeout(10_000);
        restTemplate = new RestTemplate(factory);
    }

    public SendEmailResponse sendCurriculum(SendEmailRequest req) {
        Map<String, Object> body = Map.of(
            "from", fromAddress,
            "to",   List.of(req.email()),
            "subject", "[SW Maestro] 나만의 학습 커리큘럼이 도착했습니다",
            "text", req.markdown()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        restTemplate.postForObject(RESEND_URL, new HttpEntity<>(body, headers), Map.class);

        return new SendEmailResponse("sent");
    }
}
