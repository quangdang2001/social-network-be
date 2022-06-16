package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;


    @PutMapping("/report/post/{postId}")
    private ResponseEntity<?> reportPost(@PathVariable Long postId){
        Boolean report = postService.reportPost(postId);
        if (report){
            return ResponseEntity.ok(new ResponseDTO(true,"Report success",null));
        }
        return ResponseEntity.ok(new ResponseDTO(false,"Report failed",null));
    }
    @PutMapping("/report/user/{userId}")
    private ResponseEntity<?> reportUser(@PathVariable Long userId){
        Boolean report = userService.reportUser(userId);
        if (report){
            return ResponseEntity.ok(new ResponseDTO(true,"Report success",null));
        }
        return ResponseEntity.ok(new ResponseDTO(false,"Report failed",null));
    }
}
