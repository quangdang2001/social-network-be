package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.dto.UserFollowDTO;
import com.cnpm.socialmedia.model.UserFollowing;
import com.cnpm.socialmedia.model.Users;

import java.util.List;

public interface UserFollowingService {
    List<UserFollowDTO> findAllFollowingUser(Long userId);
    List<Users> findAllIdFollowingUser(Long userId);
    List<UserFollowDTO> findAllFollowerUser(Long userId);
    List<Long> findAllIdFollower(Long userId);
    NotificationPayload save(Users users, Users following);
    void delete(Users users, Users following);
    UserFollowing checkFollow(Long userId, Long followingId);

    List<UserFollowDTO> top10Follower();
}
