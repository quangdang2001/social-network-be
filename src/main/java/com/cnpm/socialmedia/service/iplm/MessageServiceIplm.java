package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.MessageDTO;
import com.cnpm.socialmedia.model.Message;
import com.cnpm.socialmedia.repo.MessageRepo;
import com.cnpm.socialmedia.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceIplm implements MessageService {

    private final MessageRepo messageRepo;

    @Override
    public Message sendMessage(Message message) {
        return messageRepo.save(message);
    }

    @Override
    public List<MessageDTO> getMessage(Long senderId, Long receiverId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        List<Message> messages = messageRepo.findBySender_IdAndReceiver_IdOrderByCreateTimeDesc(senderId,receiverId,pageable);
        List<MessageDTO> messageDTOS = new ArrayList<>();
        MessageDTO messageDTO = new MessageDTO();
        messages.forEach(message -> {
            messageDTO.setId(message.getId());
            messageDTO.setMessage(message.getMessage());
            messageDTO.setCreateTime(message.getCreateTime());

            messageDTO.setSender(new MessageDTO.Sender(message.getSender().getId(),
                    message.getSender().getFirstName(),message.getSender().getLastName(),
                    message.getSender().getImageUrl()));

            messageDTO.setReceiver(new MessageDTO.Receiver(message.getReceiver().getId(),
                    message.getReceiver().getFirstName(),message.getReceiver().getLastName(),
                    message.getReceiver().getImageUrl()));
            messageDTOS.add(messageDTO);
        });

        return messageDTOS;
    }


    @Override
    public void deleteMessage(Long messageId) {
        messageRepo.deleteById(messageId);
    }
}
