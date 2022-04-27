package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.UserFollowing;
import com.cnpm.socialmedia.model.Users;

import java.util.List;

public interface UserFollowingService {
    List<UserDTO> findAllFollowingUser(Long userId);
    List<Long> findAllIdFollowingUser(Long userId);
    List<UserDTO> findAllFollowerUser(Long userId);
    UserFollowing save(Users users, Users following);
    void delete(Users users, Users following);
    UserFollowing checkFollow(Long userId, Long followingId);
}
