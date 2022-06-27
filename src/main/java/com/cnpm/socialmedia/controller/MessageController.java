package com.cnpm.socialmedia.controller;


import com.cnpm.socialmedia.dto.MessageDTO;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.dto.UserChatDTO;
import com.cnpm.socialmedia.model.Message;
import com.cnpm.socialmedia.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/message")
    private ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO){
        Message message =messageService.sendMessage(messageDTO);
        if (message!=null){
            messageDTO.setCreateTime(new Date());
            return ResponseEntity.ok(new ResponseDTO(true,"Success",messageDTO));
        }else {
            return ResponseEntity.ok(new ResponseDTO(false,"Failed",null));
        }
    }

    @GetMapping("/message")
    private ResponseEntity<?> getMessage(@RequestParam Long senderId,
                                         @RequestParam Long receiverId,
                                         @RequestParam int page,
                                         @RequestParam int size){
        List<MessageDTO> messageDTOList = messageService.getMessage(senderId,receiverId,page,size);
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
    private ResponseEntity<?> getConversations(@RequestParam Long userId,
                                               @RequestParam int page,
                                               @RequestParam int size){
        List<UserChatDTO> userChatDTOS = messageService.findUserChat(userId,page,size);
        return ResponseEntity.ok(
                new ResponseDTO(true,"Success",userChatDTOS));

    }

}
