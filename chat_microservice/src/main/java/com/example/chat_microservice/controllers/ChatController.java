package com.example.chat_microservice.controllers;

import com.example.chat_microservice.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @MessageMapping("/message")
    public Message receiveMessage(@Payload Message message) {
            simpMessagingTemplate.convertAndSend("/chatroom/public", message);
            return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
           simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
           return message;
    }
}
