package com.cnpm.socialmedia.controller;


import com.cloudinary.utils.ObjectUtils;


import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.dto.PostReq;
import com.cnpm.socialmedia.dto.ResponseDTO;

import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.service.Cloudinary.CloudinaryUpload;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.utils.Convert;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "AUTHORIZATION")
public class PostController {

    private final PostService postService;

    @GetMapping("/post")
    public ResponseEntity<?> getPostById(@RequestParam Long postId) throws ParseException {
        PostDTO postDTO = postService.findPostDTOById(postId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                postDTO));
    }
    @PostMapping(path = "/post")
    public ResponseEntity<?> savePost(@RequestBody PostReq postReq)  {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postService.saveNewPost(postReq, Long.parseLong(principal));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                post));
    }

    @PutMapping(path = "/post/upImg",consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upImg(@RequestParam(value = "img")MultipartFile file,
                                   @RequestParam Long postId) throws IOException {
        String imgUrl = postService.upImagePost(file, postId);
        if (imgUrl!=null)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",imgUrl));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(false,"Failed",null));
    }

    @PutMapping("/post")
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO){
        Post post = postService.updatePost(postDTO);
        return ResponseEntity.ok(new ResponseDTO(true,"Update successfully",post));
    }
    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        try {
            postService.deletePostById(id);
            return ResponseEntity.ok(new ResponseDTO(true,"Delete successfully",null));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ResponseDTO(false,e.getMessage(),null));

        }
    }
    @GetMapping("/post/user")
    public ResponseEntity<?> getAllPostUser(@RequestParam(defaultValue = "-1") Long userId,
                                            @RequestParam(value = "page",defaultValue = "0") Integer page,
                                            @RequestParam(value = "size",defaultValue = "10") Integer size){
        List<PostDTO> posts;
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userId.toString().equals(principal))
            posts = postService.findPostOfUser(userId,null,page,size);
        else posts = postService.findPostOfUser(userId,Long.parseLong(principal),page,size);

        return ResponseEntity.ok(new ResponseDTO(true,"Success",posts));
    }
    @GetMapping("/post/homepage")
    public ResponseEntity<?> getPostHomepage(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                             @RequestParam(value = "size",defaultValue = "10") Integer size){
        List<PostDTO> posts;
        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        posts = postService.findPostHomePage(Long.parseLong(principal),page, size);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",posts));
    }

    @PostMapping("/post/like")
    public ResponseEntity<?> likePost(@RequestParam Long postId) throws InterruptedException {
        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        NotificationPayload notificationPayload = postService.likePost(Long.parseLong(principal), postId);
        if (notificationPayload!= null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true, "Success", notificationPayload));
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true, "Success", null));
        }
    }




}
