package com.cnpm.socialmedia.controller.ws.controller;

import com.cnpm.socialmedia.controller.ws.Payload.Like;
import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.controller.ws.Payload.UsActive;
import com.cnpm.socialmedia.dto.CmtDTO;
import com.cnpm.socialmedia.dto.CmtResponse;
import com.cnpm.socialmedia.model.*;
import com.cnpm.socialmedia.service.*;
import com.cnpm.socialmedia.utils.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationControllerWS {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final UserFollowingService userFollowingService;

    @MessageMapping("/active-status")
    public ResponseEntity<?> sendActiveToAllUser(@Payload Long userId){
        Users users = userService.findById(userId);
        String fullName = users.getLastName()+" "+ users.getFirstName();
        String email = users.getEmail();
        UsActive usActive = new UsActive(fullName,email);
        List<Long> userIds = userFollowingService.findAllIdFollower(userId);
        for (Long id : userIds){
            System.out.println("Send online status to: "+ id);
            simpMessagingTemplate.convertAndSendToUser(id.toString(),"/online",usActive);
        }
        return ResponseEntity.ok(usActive);
    }

    @MessageMapping("/sendNotification")
    public ResponseEntity<?> sendNotificationCmt(@Payload NotificationPayload notificationPayload){
        notificationPayload.setType("NOTIFICATION");
        simpMessagingTemplate.convertAndSendToUser(notificationPayload.getUserReceiverId().toString(),
                "/notificationPopUp",notificationPayload);

        return ResponseEntity.ok(notificationPayload);
    }




}
