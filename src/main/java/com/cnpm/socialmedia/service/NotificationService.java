package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.dto.NotificationDTO;
import com.cnpm.socialmedia.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification save(Notification notification);
    void deleteById(Long id);
    List<NotificationDTO> findNotificationByUserId(Long userId, Integer page, Integer size);
    Notification findById(Long id);
    Long countNotSeenNotifi(Long userId);

}
