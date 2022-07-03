package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.MessageDTO;
import com.cnpm.socialmedia.dto.MessageSendDTO;
import com.cnpm.socialmedia.dto.UserChatDTO;
import com.cnpm.socialmedia.model.Message;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.MessageRepo;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.MessageService;
import com.cnpm.socialmedia.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceIplm implements MessageService {
    private final MessageRepo messageRepo;
    private final UserService userService;
    private final UserRepo userRepo;


    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        Message message = new Message();
        if (!userRepo.existsById(messageDTO.getSenderId()) || !userRepo.existsById(messageDTO.getReceiverId()))
            return null;
        Users usersSend = userRepo.getById(messageDTO.getSenderId());
        Users usersReceiver = userRepo.getById(messageDTO.getReceiverId());

        message.setMessage(messageDTO.getMessage());
        message.setCreateTime(new Date());
        message.setSender(usersSend);
        message.setReceiver(usersReceiver);
        Long receiverId = messageDTO.getReceiverId();
        Long senderId = messageDTO.getSenderId();
        message.setRoom(getRoom(receiverId, senderId));
        messageRepo.save(message);
        messageDTO.setRoom(getRoom(receiverId, senderId));
        messageDTO.setCreateTime(new Date());

        return messageDTO;


    }

    @Override
    public List<MessageDTO> getMessage(Long senderId, Long receiverId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        String room = getRoom(receiverId, senderId);
        List<Message> messages = messageRepo.findAllByRoomOrderByCreateTimeDesc(room, pageable);
        List<MessageDTO> messageDTOS = new ArrayList<>();
        messages.forEach(message -> {
            messageDTOS.add(new MessageDTO(message.getId(), message.getMessage(),
                    message.getCreateTime(), message.getSender().getId(),
                    message.getReceiver().getId(), message.getSender().getImageUrl(), room));
        });
        Collections.reverse(messageDTOS);
        return messageDTOS;
    }


    @Override
    public boolean deleteMessage(Long messageId) {
        try {
            messageRepo.deleteById(messageId);
            return true;
        } catch (Exception e) {
            log.info("Message exception: ", e.getMessage());
            return false;
        }
    }

    @Override
    public List<UserChatDTO> findUserChat(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Users> usersSender = messageRepo.findSenderChat(userId, pageable);
        List<Users> userReceiver = messageRepo.findReceiverChat(userId, pageable);
        int length = userReceiver.size();
        List<Users> conversations = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (!usersSender.get(i).getId().equals(userId)) {
                conversations.add(usersSender.get(i));
            } else
                conversations.add(userReceiver.get(i));
        }

        conversations = conversations.stream().distinct().collect(Collectors.toList());
        conversations.forEach(conversation -> System.out.println(conversation.getEmail()));
        List<UserChatDTO> userChatDTOS = new ArrayList<>();
        conversations.forEach(user -> {
            userChatDTOS.add(new UserChatDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getImageUrl()
                    , user.getEmail(), getRoom(userId, user.getId())));
        });
        return userChatDTOS;
    }

    public String getRoom(Long receiverId, Long senderId) {
        return String.valueOf(receiverId + senderId) + String.valueOf(receiverId * senderId);
    }
}
