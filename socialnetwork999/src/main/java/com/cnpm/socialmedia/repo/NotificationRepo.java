package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserReceiver_IdOrderByCreateTimeDesc(Long userId, Pageable pageable);
//    @Query("select count(n) from Notification n where n.userReceiver=:receiverId and n.isSeen =: FALSE")
    List<Notification> findNotificationByUserReceiver_Id(Long userId);
}
