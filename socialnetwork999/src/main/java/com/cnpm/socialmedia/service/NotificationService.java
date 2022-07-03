package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.NotificationDTO;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;

import java.util.List;

public interface NotificationService {
    Notification save(Notification notification);

    void deleteById(Long id);

    List<NotificationDTO> findNotificationByUserId(Long userId, Integer page, Integer size);

    Notification findById(Long id);

    Long countNotSeenNotifi(Long userId);

    Notification sendNotificationPost(Post post, Users senderId, String content);

    Notification sendNotificationFollow(Users user, Users userReceiver, String content);

}
