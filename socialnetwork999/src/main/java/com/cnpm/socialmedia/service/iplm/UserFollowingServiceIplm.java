package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.dto.UserFollowDTO;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.UserFollowing;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.UserFollowingRepo;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.service.UserFollowingService;
import com.cnpm.socialmedia.utils.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFollowingServiceIplm implements UserFollowingService {

    private final UserFollowingRepo userFollowingRepo;
    private final NotificationService notificationService;
    private final UserRepo userRepo;
    @Override
    public List<UserFollowDTO> findAllFollowingUser(Long userId) {
        List<UserFollowing> userFollowings = userFollowingRepo.findAllByUserId_Id(userId);
        List<Users> users = new ArrayList<>();
        userFollowings.forEach(userFollowing -> {
            users.add(userFollowing.getFollowingId());
        });

        return convertUsersToUserFollowDTO(users);
    }

    @Override
    public List<Users> findAllIdFollowingUser(Long userId) {
        List<UserFollowing> userFollowings = userFollowingRepo.findAllByUserId_Id(userId);
        List<Users> usersId = new ArrayList<>();
        userFollowings.forEach(userFollowing -> {
            usersId.add(userFollowing.getFollowingId());
        });
        return usersId;
    }

    @Override
    public List<UserFollowDTO> findAllFollowerUser(Long userId) {
        List<UserFollowing> userFollowings = userFollowingRepo.findAllByFollowingId_Id(userId);
        List<Users> users = new ArrayList<>();
        userFollowings.forEach(userFollowing -> {
            users.add(userFollowing.getUserId());
        });

        return convertUsersToUserFollowDTO(users);
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
    public NotificationPayload save(Users users, Users following) {
        UserFollowing userFollowing = new UserFollowing(users,following);
        String content = String.format("%s %s followed you.",users.getLastName(), users.getFirstName());
        Notification notification= notificationService.sendNotificationFollow(users,following,content);
        userFollowingRepo.save(userFollowing);

        return Convert.convertNotificationToNotifiPayload(notification);
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



    @Override
    public List<UserFollowDTO> top10Follower() {
        Pageable pageable = PageRequest.of(0,5);
        List<Users> users =  userRepo.findTop10Follower(pageable);

        List<UserFollowDTO> userFollowDTOS = new ArrayList<>();
        users.forEach(user ->{
            userFollowDTOS.add(new UserFollowDTO(user.getId(),user.getFirstName(),user.getLastName(),
                    user.getEmail(),user.getImageUrl(),user.getCountFollower()));
        } );
        return userFollowDTOS;
    }

    private List<UserFollowDTO> convertUsersToUserFollowDTO(List<Users> users){
        List<UserFollowDTO> userDTOList = new ArrayList<>();
        users.forEach(user -> {
            userDTOList.add(new UserFollowDTO(user.getId(),user.getFirstName(),user.getLastName(),
                    user.getEmail(),user.getImageUrl(),user.getCountFollower()));
        });
        return userDTOList;
    }
}

