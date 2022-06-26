package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.dto.MessageDTO;
import com.cnpm.socialmedia.dto.MessageSendDTO;
import com.cnpm.socialmedia.model.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(MessageDTO messageDTO);
    List<MessageDTO> getMessage(Long senderId, Long receiverId,Integer page, Integer size);
    boolean deleteMessage(Long messageId);
}
