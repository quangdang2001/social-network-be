package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.utils.Constant;
import com.cnpm.socialmedia.utils.Convert;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "AUTHORIZATION")
public class AdminController {

    private final UserService userService;
    private final PostService postService;


    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam int page,
                                     @RequestParam int size){
        Page<Users> usersList = userService.getUserAdmin(page,size);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",usersList));
    }
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId){
        boolean check = userService.deleteUser(userId);
        if (check){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
        }
        else {
            return ResponseEntity.ok(new ResponseDTO(false,"Failed",null));
        }
    }


    @GetMapping("/report/user")
    private ResponseEntity<?> getUserReported(){
        List<Users> users = userService.findUserReported();
        List<UserDTO> userDTOList = new ArrayList<>();
        UserDTO userDTO = new UserDTO();
        users.forEach(user ->{
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(userDTO.getEmail());
            userDTO.setCountReport(user.getCountReport());
            userDTO.setImageUrl(user.getImageUrl());
            userDTOList.add(userDTO);
        });
        return ResponseEntity.ok(new ResponseDTO(true,"Success",userDTOList));
    }
    @GetMapping("/report/post")
    private ResponseEntity<?> getPostReported(){
        List<Post> posts = postService.findPostReported();
        return ResponseEntity.ok(new ResponseDTO(true,"Success",posts));

    }
    @GetMapping("/post")
    public ResponseEntity<?> getAllPost(@RequestParam int page,
                      @RequestParam int size){
        JSONArray result = postService.getPostAdmin(page,size);
        return ResponseEntity.ok(result);
    }

    @PutMapping("user/enable")
    private ResponseEntity<?> enable(@RequestParam String email){
        userService.enableUser(email);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));


    }
}
