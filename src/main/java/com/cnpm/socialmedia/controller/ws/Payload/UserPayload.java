package com.cnpm.socialmedia.controller.ws.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPayload {
    private String firstName;
    private String lastName;
    private Long userId;
    private String avatar;
}
