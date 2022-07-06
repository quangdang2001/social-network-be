package com.cnpm.socialmedia.controller.ws.Payload;

import lombok.Data;

import java.util.Date;

@Data
public class MessagePayload {
    private Long id;
    private String message;
    private Date createTime;
    private Long senderId;
    private Long receiverId;
    private String senderAvatar;
    private String room;
    private String fullName;
}
