package com.cnpm.socialmedia.controller.ws.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPayload {
    private String firstName;
    private String lastName;
    private Long userId;
    private String avatar;
}
