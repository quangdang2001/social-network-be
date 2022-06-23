package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.UserFollowing;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.NotificationRepo;
import com.cnpm.socialmedia.repo.UserFollowingRepo;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.service.UserFollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service

@RequiredArgsConstructor
public class UserFollowingServiceIplm implements UserFollowingService {

    private final UserFollowingRepo userFollowingRepo;
    private final NotificationService notificationService;
    @Override
    public List<UserDTO> findAllFollowingUser(Long userId) {
        List<UserFollowing> userFollowings = userFollowingRepo.findAllByUserId_Id(userId);
        List<Users> users = new ArrayList<>();
        userFollowings.forEach(userFollowing -> {
            users.add(userFollowing.getFollowingId());
        });

        List<UserDTO> userDTOList = new ArrayList<>();
        users.forEach(user -> {
            userDTOList.add(new UserDTO(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),
                    user.getImageUrl()));
        });
        return userDTOList;
    }

    @Override
    public List<Long> findAllIdFollowingUser(Long userId) {
        List<UserFollowing> userFollowings = userFollowingRepo.findAllByUserId_Id(userId);
        List<Long> usersId = new ArrayList<>();
        userFollowings.forEach(userFollowing -> {
            usersId.add(userFollowing.getFollowingId().getId());
        });
        return usersId;
    }

    @Override
    public List<UserDTO> findAllFollowerUser(Long userId) {
        List<UserFollowing> userFollowings = userFollowingRepo.findAllByFollowingId_Id(userId);
        List<Users> users = new ArrayList<>();
        userFollowings.forEach(userFollowing -> {
            users.add(userFollowing.getUserId());
        });
        List<UserDTO> userDTOList = new ArrayList<>();
        users.forEach(user -> {
            userDTOList.add(new UserDTO(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),
                    user.getImageUrl()));
        });
        return userDTOList;
    }

    @Override
    public List<Long> findAllIdFollower(Long userId) {
        List<UserFollowing> userFollowings = userFollowingRepo.findAllByFollowingId_Id(userId);
        List<Long> usersId = new ArrayList<>();
        userFollowings.forEach(userFollowing -> {
            usersId.add(userFollowing.getUserId().getId());
        });
        return usersId;
    }

    @Override
    public UserFollowing save(Users users, Users following) {
        UserFollowing userFollowing = new UserFollowing(users,following);
        String content = String.format("%s %s followed you.",users.getFirstName(), users.getLastName());
        notificationService.sendNotificationFollow(users,following,content);
        UserFollowing userFollowing1 = userFollowingRepo.save(userFollowing);
        return userFollowing1;
    }

    @Override
    public void delete(Users users, Users following) {
        UserFollowing userFollowing = new UserFollowing(users,following);
        userFollowingRepo.delete(userFollowing);
    }

    @Override
    public UserFollowing checkFollow(Long userId, Long followingId) {
        return userFollowingRepo.findByUserId_IdAndFollowingId_Id(userId,followingId);
    }
}
