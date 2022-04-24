package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.MessageDTO;
import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) throws ParseException {
        Post post = postService.findPostById(id);
        PostDTO postDTO = new PostDTO();

        postDTO.setUserId(post.getUsers().getId());
        postDTO.setId(post.getId());
        postDTO.setContent(post.getContent());
        postDTO.setUrlImage(post.getImgUrl());
        postDTO.setCountCmted(post.getCountCmted());
        postDTO.setCountLiked(post.getCountLiked());
        postDTO.setCountShated(post.getCountShated());
        postDTO.setCountReported(post.getCountReported());
        postDTO.setCreateTime(post.getCreateTime());
        postDTO.setUpdateTime(post.getUpdateTime());

        return ResponseEntity.ok(postDTO);
    }
    @PostMapping("/post")
    public Post savePost(@RequestBody PostDTO postDTO){
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setImgUrl(postDTO.getUrlImage());
        post.setUsers(userService.findById(postDTO.getUserId()));
        post.setCreateTime(new Date());
        System.out.println(post.getCreateTime());
        return postService.save(post);
    }

    @PutMapping("/post")
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO){
        Post post = postService.findPostById(postDTO.getId());
        post.setContent(postDTO.getContent());
        post.setImgUrl(post.getImgUrl());
        post.setUpdateTime(new Date());
        postService.save(post);
        return ResponseEntity.ok(new MessageDTO(true,"Update successfully"));
    }
    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        try {
            postService.deletePostById(id);
            return ResponseEntity.ok(new MessageDTO(true,"Delete successfully"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(false,"Delete failed"));
        }
    }
    @GetMapping("/post/user/{id}")
    public ResponseEntity<?> getAllPostUser(@PathVariable Long id){
        List<Post> posts = new ArrayList<>();
        posts = postService.findPostOfUser(id);
        return ResponseEntity.ok(posts);
    }


}
