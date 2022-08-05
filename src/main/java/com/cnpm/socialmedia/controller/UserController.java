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
import com.cnpm.socialmedia.utils.Constant;
import com.cnpm.socialmedia.utils.Convert;
import com.cnpm.socialmedia.utils.Utils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@SecurityRequirement(name = "AUTHORIZATION")
public class UserController {

    private final UserService userService;
    private final UserFollowingService userFollowingService;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/user/follow")
    public ResponseEntity<?> followUser(@RequestParam Long userFollowedId){

        NotificationPayload notificationPayload = userService.followUser(userFollowedId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                notificationPayload));

    }
    @GetMapping("/user/following")
    public ResponseEntity<?> getFollowing(){
        Long userId = Utils.getIdCurrentUser();
        List<UserFollowDTO> userDTOList = userFollowingService.findAllFollowingUser(userId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                userDTOList));
    }

    @GetMapping("/user/follower")
    public ResponseEntity<?> getFollower(){
        Long userId = Utils.getIdCurrentUser();
        List<UserFollowDTO> userDTOList = userFollowingService.findAllFollowerUser(userId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                userDTOList));
    }

    @GetMapping("/user/personalPage")
    public ResponseEntity<?> seePersonalPage(@RequestParam Long personalPageId){
        PersonalPage personalPage = userService.seePersonalPage(personalPageId);
        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                personalPage));

    }

    @GetMapping("/user/notification")
    private ResponseEntity<?> getNotifi(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size){
        Long userId = Utils.getIdCurrentUser();
        List<NotificationDTO> notificationDTOS = notificationService.findNotificationByUserId(userId,page,size);

        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                notificationDTOS));
    }


    @PostMapping(path = "/user/upimg",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upUserImgProfile(@RequestParam("img")MultipartFile file) throws IOException {
        String imgUrl = userService.upImageProfile(file);

        return ResponseEntity.ok().body(new ResponseDTO(true,"Success",
                imgUrl));
    }

    @GetMapping("/user")
    private ResponseEntity<?> getUser(){
        Long userId = Utils.getIdCurrentUser();
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

    @PostMapping("/user/registerTest")
    public ResponseEntity<?> registerAdmin(@RequestBody UserDTO userDTO){
        Users user;
        user = userService.findUserByEmail(userDTO.getEmail());
        if (user!=null){
            return null;
        }
        user = new Users();
        user.setFirstName(Convert.formatName(userDTO.getFirstName()));
        user.setLastName(Convert.formatName(userDTO.getLastName()));
        user.setAddress(userDTO.getAddress());
        user.setBio(userDTO.getBio());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setBirthDay(userDTO.getBirthDay());
        user.setEnable(true);
        user.setRole(Constant.ROLE_ADMIN);
        userService.save(user);
        return ResponseEntity.ok(user);
    }

}
