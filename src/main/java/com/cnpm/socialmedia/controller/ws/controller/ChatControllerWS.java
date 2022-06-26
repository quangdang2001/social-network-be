package com.cnpm.socialmedia.controller.ws.controller;

import com.cnpm.socialmedia.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatControllerWS {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@Payload MessageDTO messageDTO){
        simpMessagingTemplate.convertAndSendToUser(messageDTO.getReceiverId().toString(),"/chat",messageDTO);
        return ResponseEntity.ok(messageDTO);
    }
}
