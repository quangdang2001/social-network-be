package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PostShareController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @PostMapping("/postshare")
    public ResponseEntity<?> savePostShare(@RequestBody PostDTO postDTO){
        Post post = new Post();
        post.setPostShare(true);
        post.setContent(postDTO.getContent());
        post.setUsers(userService.findById(postDTO.getUserId()));
        post.setPostShared(postService.findPostById(postDTO.getPostSharedId()));
        post.setCreateTime(new Date());
        postService.save(post);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                post));
    }
}
