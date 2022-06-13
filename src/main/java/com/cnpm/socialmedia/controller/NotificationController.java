package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PutMapping("notification/setSeen")
    public ResponseEntity<?> setSeen(@RequestParam Long notificationId){
       Notification notification = notificationService.findById(notificationId);
       notification.setSeen(true);
       notificationService.save(notification);
       return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                null));
    }
}
