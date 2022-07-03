package com.cnpm.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalPage {
    private Long id;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;
    private int gender;
    private String bio;
    private String address;
    private boolean enable;
    private int countFollower;
    private int countFollowing;
    private boolean follow;
}
