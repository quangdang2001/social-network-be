package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "AUTHORIZATION")
public class PostShareController {
    private final UserService userService;
    private final PostService postService;
    @PostMapping("/postshare")
    public ResponseEntity<?> savePostShare(@RequestBody PostDTO postDTO){

        NotificationPayload notificationPayload= postService.sharePost(postDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                notificationPayload));
    }
}
