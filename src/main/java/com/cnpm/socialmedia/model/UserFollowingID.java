package com.cnpm.socialmedia.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor

public class UserFollowingID implements Serializable {
    private Long userId;
    private Long followingId;
}
