package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.MessageDTO;
import com.cnpm.socialmedia.dto.MessageSendDTO;
import com.cnpm.socialmedia.dto.UserChatDTO;
import com.cnpm.socialmedia.model.Message;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.MessageRepo;
import com.cnpm.socialmedia.service.MessageService;
import com.cnpm.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceIplm implements MessageService {
    private final MessageRepo messageRepo;
    private final UserService userService;


    @Override
    public Message sendMessage(MessageDTO messageDTO) {
        Message message = new Message();
        Users usersSend = userService.findById(messageDTO.getSenderId());
        Users usersReceiver = userService.findById(messageDTO.getReceiverId());
        if (usersSend != null && usersReceiver !=null) {
            message.setMessage(messageDTO.getMessage());
            message.setCreateTime(new Date());
            message.setSender(usersSend);
            message.setReceiver(usersReceiver);
            message.setRoom(String.valueOf(messageDTO.getReceiverId()+messageDTO.getSenderId()));
            return messageRepo.save(message);
        }
        return null;
    }

    @Override
    public List<MessageDTO> getMessage(Long senderId, Long receiverId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        String room = String.valueOf(senderId+receiverId);
        List<Message> messages = messageRepo.findAllByRoomOrderByCreateTimeDesc(room,pageable);
        List<MessageDTO> messageDTOS = new ArrayList<>();
        messages.forEach(message -> {
            if (message.getReceiver().getId() != receiverId){
                messageDTOS.add(new MessageDTO(message.getId(),message.getMessage(),
                        message.getCreateTime(),message.getSender().getId(),message.getReceiver().getId(),null));
            }
            else {
                messageDTOS.add(new MessageDTO(message.getId(),message.getMessage(),
                        message.getCreateTime(),message.getSender().getId(),message.getReceiver().getId(),message.getReceiver().getImageUrl()));
            }

        });
        Collections.reverse(messageDTOS);
        return messageDTOS;
    }


    @Override
    public boolean deleteMessage(Long messageId) {
        try {
            messageRepo.deleteById(messageId);
            return true;
        }catch (Exception e){
            log.info("Message exception: ",e.getMessage());
            return false;
        }
    }

    @Override
    public List<UserChatDTO> findUserChat(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        List<Users> users = messageRepo.findUserChat(userId,pageable);
        List<UserChatDTO> userChatDTOS = new ArrayList<>();
        users.forEach(user ->{
            userChatDTOS.add(new UserChatDTO(user.getId(),user.getFirstName(),user.getLastName(),user.getImageUrl()
            ,user.getEmail()));
        });
        return userChatDTOS;
    }
}
