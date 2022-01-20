package com.ktsnvt.ktsnvt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.sendnotification.NotificationMessageDTO;
import com.ktsnvt.ktsnvt.exception.InvalidMessageFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
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
    @MessageMapping({"send/message"})
    public NotificationMessageDTO broadcastNotification(NotificationMessageDTO notification) {
        Map<String, String> message = new HashMap<>();
        message.put("fromId", notification.getFromId());
        message.put("message", notification.getMessage());

        this.simpMessagingTemplate.convertAndSend("/socket-publisher", message);

        return notification;
    }
}
