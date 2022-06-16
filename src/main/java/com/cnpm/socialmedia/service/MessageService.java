package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.dto.MessageDTO;
import com.cnpm.socialmedia.dto.MessageSendDTO;
import com.cnpm.socialmedia.model.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(MessageSendDTO messageSendDTO);
    List<MessageDTO> getMessage(Long senderId, Long receiverId,Integer page, Integer size);
    void deleteMessage(Long messageId);
}
