package com.example.emailsendproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    //key, value data-type을 선언
    //kafka를 조작할 수 있게 만들어 주는 객체
    private final KafkaTemplate<String ,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public EmailService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void sendEmail(SendEmailRequestDTO request) {
        EmailSendMessage emailSendMessage = new EmailSendMessage(
                request.getFrom(),
                request.getTo(),
                request.getSubject(),
                request.getBody()
        );

        this.kafkaTemplate.send("email.send", toJsonString(emailSendMessage));
    }

    private String toJsonString(Object object){

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
