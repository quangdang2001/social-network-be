package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.PersonalPage;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.UserFollowing;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.UserFollowingService;
import com.cnpm.socialmedia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFollowingService userFollowingService;

    @PostMapping("/user/follow")
    public ResponseEntity<?> followUser(@RequestParam Long userId,
                                        @RequestParam Long userFollowedId){
        Users users = userService.findById(userId);
        Users userFollowed = userService.findById(userFollowedId);
        UserFollowing checkFollow = userFollowingService.checkFollow(userId,userFollowedId);
        if (checkFollow == null) {
            userFollowingService.save(users,userFollowed);
            users.setCountFollowing(users.getCountFollowing()+1);
            userFollowed.setCountFollower(userFollowed.getCountFollower()+1);
            userService.save(users);
            userService.save(userFollowed);
        }else {
            userFollowingService.delete(users,userFollowed);
            users.setCountFollowing(users.getCountFollowing()-1);
            userFollowed.setCountFollower(userFollowed.getCountFollower()-1);
            userService.save(users);
            userService.save(userFollowed);
        }

        return ResponseEntity.ok().build();
    }
    @GetMapping("/user/following")
    public ResponseEntity<?> getFollowing(@RequestParam Long userId){
        List<UserDTO> userDTOList = userFollowingService.findAllFollowingUser(userId);

        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/user/follower")
    public ResponseEntity<?> getFollower(@RequestParam Long userId){
        List<UserDTO> userDTOList = userFollowingService.findAllFollowerUser(userId);

        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/user/personalPage")
    public ResponseEntity<?> seepersonalPage(@RequestParam Long userId,
                                             @RequestParam Long personalPageId){
        Users users = userService.findById(personalPageId);
        UserFollowing checkFollowing = userFollowingService.checkFollow(userId,personalPageId);
        PersonalPage personalPage = new PersonalPage();
        personalPage.setId(users.getId());
        personalPage.setFirstName(users.getFirstName());
        personalPage.setLastName(users.getLastName());
        personalPage.setEmail(users.getEmail());
        personalPage.setImageUrl(users.getImageUrl());
        personalPage.setGender(users.getGender());
        personalPage.setBio(users.getBio());
        personalPage.setAddress(users.getAddress());
        personalPage.setEnable(users.isEnable());
        personalPage.setCountFollower(users.getCountFollower());
        personalPage.setCountFollowing(users.getCountFollowing());
        personalPage.setFollow(checkFollowing != null);
        return ResponseEntity.ok(personalPage);
    }


}
