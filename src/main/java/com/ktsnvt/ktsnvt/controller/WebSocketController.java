package com.ktsnvt.ktsnvt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.exception.InvalidMessageFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /*
     * WebSocket endpoint
     * Message is sent to all clients subscribed on /socket-publisher topic.
     */
    @MessageMapping("send/message")
    public Map<String, String> broadcastNotification(String notificationJSON) {
        Map<String, String> convertedMessage = parseMessage(notificationJSON);

        this.simpMessagingTemplate.convertAndSend("/socket-publisher", convertedMessage);

        return convertedMessage;
    }

    @SuppressWarnings("unchecked")
    // because we are parsing a JSON object into Map<String, String> cast is always possible
    private Map<String, String> parseMessage(String message) {
        var mapper = new ObjectMapper();
        Map<String, String> parsedMessage;

        try {
            parsedMessage = mapper.readValue(message, Map.class);
        } catch (IOException e) {
            throw new InvalidMessageFormatException("Message format is invalid.");
        }

        return parsedMessage;
    }
}
