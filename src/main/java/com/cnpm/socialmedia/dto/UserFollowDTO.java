package com.cnpm.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private int countFollower;
}
