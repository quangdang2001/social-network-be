package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.NotificationDTO;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.NotificationRepo;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.utils.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceIplm implements NotificationService {

    private final NotificationRepo notificationRepo;

    @Override
    public Notification save(Notification notification) {
        return notificationRepo.save(notification);
    }

    @Override
    public void deleteById(Long id) {
        notificationRepo.deleteById(id);
    }

    @Override
    public List<NotificationDTO> findNotificationByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Notification> notifications =
                notificationRepo.findAllByUserReceiver_IdOrderByCreateTimeDesc(userId,pageable);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        notifications.forEach(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            UserDTO userDTO = new UserDTO();
            notificationDTO.setContent(notification.getContent());
            notificationDTO.setId(notification.getId());
            notificationDTO.setSeen(notification.isSeen());
            notificationDTO.setUserReceiver(notification.getUserReceiver().getId());
            notificationDTO.setCreateTime(notification.getCreateTime());
            if (notification.getPost()!= null) {
                notificationDTO.setPostId(notification.getPost().getId());
            }
            userDTO.setId(notification.getUserCreate().getId());
            userDTO.setLastName(notification.getUserCreate().getLastName());
            userDTO.setFirstName(notification.getUserCreate().getFirstName());
            userDTO.setImageUrl(notification.getUserCreate().getImageUrl());

            notificationDTO.setUserCreate(userDTO);
            notificationDTOS.add(notificationDTO);
        });
        return notificationDTOS;
    }

    @Override
    public Notification findById(Long id) {
        Optional<Notification> optional = notificationRepo.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Long countNotSeenNotifi(Long userId) {
        Long count = notificationRepo.findNotificationByUserReceiver_Id(userId).stream().filter(notification ->
                !notification.isSeen()).count();
        return count;

    }

    @Override
    public Notification sendNotificationPost(Post post, Users senderId, String content) {
            Notification notification = new Notification();
            notification.setContent(content);
            notification.setUserReceiver(post.getUsers());
            notification.setUserCreate(senderId);
            notification.setPost(post);
            notification.setCreateTime(new Date());
            save(notification);
            return notification;
    }

    @Override
    public Notification sendNotificationFollow(Users user, Users userReceiver, String content) {
        Notification notification = new Notification();
        try {
            notification.setContent(content);
            notification.setUserCreate(user);
            notification.setUserReceiver(userReceiver);
            notification.setCreateTime(new Date());

            notificationRepo.save(notification);
        }catch (Exception e){
            return null;
        }
        return notification;
    }
}
