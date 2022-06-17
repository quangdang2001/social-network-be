package com.cnpm.socialmedia.controller.ws.Payload;

import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationControllerWS {

    @Autowired
    private PostService postService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

//    @MessageMapping("/likePost")
//    public ResponseEntity<?> sendNotifilikePost(@Payload Like like){
//        Notification notification = postService.likePost(like.getPostId(),like.getUserId());
//        if (notification!= null){
//            Long count = notificationService.countNotSeenNotifi(notification.getUserReceiver().getId());
//            simpMessagingTemplate.
//                    convertAndSendToUser(notification.getUserReceiver().getId().toString(),"/queue/private",count);
//            return ResponseEntity.ok(count);
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public Message receiveMessage(@Payload Message message){
//    return message;
//}

}
