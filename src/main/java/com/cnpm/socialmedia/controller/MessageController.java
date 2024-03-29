package com.cnpm.socialmedia.controller;


import com.cnpm.socialmedia.dto.MessageDTO;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.dto.UserChatDTO;
import com.cnpm.socialmedia.model.Message;
import com.cnpm.socialmedia.service.MessageService;
import com.cnpm.socialmedia.utils.Utils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "AUTHORIZATION")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/message")
    private ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO){
        MessageDTO message =messageService.sendMessage(messageDTO);
        if (message!=null){

            return ResponseEntity.ok(new ResponseDTO(true,"Success",message));
        }else {
            return ResponseEntity.ok(new ResponseDTO(false,"Failed",null));
        }
    }

    @GetMapping("/message")
    private ResponseEntity<?> getMessage(@RequestParam Long receiverId,
                                         @RequestParam int page,
                                         @RequestParam int size){
        Long userId = Utils.getIdCurrentUser();
        List<MessageDTO> messageDTOList = messageService.getMessage(userId,receiverId,page,size);
        return ResponseEntity.ok(new ResponseDTO(true,"Success", messageDTOList));
    }

    @DeleteMapping("/message/{messageId}")
    private ResponseEntity<?> deleteMessage(@PathVariable Long messageId){
        boolean check = messageService.deleteMessage(messageId);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else return ResponseEntity.ok(new ResponseDTO(false,"Failed",null));
    }
    @GetMapping("/message/conversations")
    private ResponseEntity<?> getConversations(@RequestParam int page,
                                               @RequestParam int size){
        Long userId = Utils.getIdCurrentUser();
        List<UserChatDTO> userChatDTOS = messageService.findUserChat(userId,page,size);
        return ResponseEntity.ok(
                new ResponseDTO(true,"Success",userChatDTOS));

    }

}
