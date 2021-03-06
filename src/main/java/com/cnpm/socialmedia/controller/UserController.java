package com.cnpm.socialmedia.controller;

import com.cloudinary.utils.ObjectUtils;
import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.*;
import com.cnpm.socialmedia.model.UserFollowing;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.Cloudinary.CloudinaryUpload;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.service.UserFollowingService;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.utils.Convert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserFollowingService userFollowingService;
    private final NotificationService notificationService;
    private final CloudinaryUpload cloudinaryUpload;

    @PostMapping("/user/follow")
    public ResponseEntity<?> followUser(@RequestParam Long userId,
                                        @RequestParam Long userFollowedId){
        Users users = userService.findById(userId);
        Users userFollowed = userService.findById(userFollowedId);
        UserFollowing checkFollow = userFollowingService.checkFollow(userId,userFollowedId);
        NotificationPayload notificationPayload = null;
        if (checkFollow == null) {
            notificationPayload = userFollowingService.save(users,userFollowed);
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
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                notificationPayload));
    }
    @GetMapping("/user/following")
    public ResponseEntity<?> getFollowing(@RequestParam Long userId){
        List<UserFollowDTO> userDTOList = userFollowingService.findAllFollowingUser(userId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                userDTOList));
    }

    @GetMapping("/user/follower")
    public ResponseEntity<?> getFollower(@RequestParam Long userId){
        List<UserFollowDTO> userDTOList = userFollowingService.findAllFollowerUser(userId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                userDTOList));
    }

    @GetMapping("/user/personalPage")
    public ResponseEntity<?> seePersonalPage(@RequestParam Long userId,
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

        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                personalPage));

    }

    @GetMapping("/user/notification")
    private ResponseEntity<?> getNotifi(@RequestParam Long userId,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size){
        List<NotificationDTO> notificationDTOS = notificationService.findNotificationByUserId(userId,page,size);

        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                notificationDTOS));
    }


    @PostMapping(path = "/user/upimg",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upUserImgProfile(@RequestParam Long userId,
                                              @RequestParam("img")MultipartFile file) throws IOException {
        Users users = userService.findById(userId);
        String imgUrl = users.getImageUrl();

        Map params = ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "avatars"
        );
        Map map = cloudinaryUpload.cloudinary().uploader().upload(Convert.convertMultiPartToFile(file),params);
        if (imgUrl!= null) {
            cloudinaryUpload.cloudinary().uploader().destroy("avatars/" + cloudinaryUpload.getPublicId(imgUrl)
                    , ObjectUtils.asMap("resource_type", "image"));
        }
        imgUrl = (String) map.get("secure_url");
        users.setImageUrl(imgUrl);
        userService.save(users);

        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                imgUrl));
    }

    @GetMapping("/user")
    private ResponseEntity<?> getUser(@RequestParam Long userId){
        Users users = userService.findById(userId);

        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                users));

    }
    @PutMapping("/user")
    private ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO){
        Users users = userService.updateUser(userDTO);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                users));
    }
    @GetMapping("/user/search")
    public ResponseEntity<?> searchUser(@RequestParam String keyword){
        Set<Users> users = userService.searchUser(keyword);
        users.forEach(user ->{
            user.setPassword(null);
        });
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                users));
    }

    @GetMapping("/user/topFollower")
    public ResponseEntity<?> getUserTop(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",userFollowingService.top10Follower()));
    }



}
