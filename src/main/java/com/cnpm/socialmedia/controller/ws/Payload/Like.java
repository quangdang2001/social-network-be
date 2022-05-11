package com.cnpm.socialmedia.controller.ws.Payload;

import lombok.Data;

@Data
public class Like {
    private Long userId;
    private Long postId;
}
