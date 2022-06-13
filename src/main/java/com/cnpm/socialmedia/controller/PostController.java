package com.cnpm.socialmedia.controller;


import com.cloudinary.utils.ObjectUtils;


import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.dto.ResponseDTO;

import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.service.Cloudinary.CloudinaryUpload;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.utils.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private CloudinaryUpload cloudinaryUpload;


    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) throws ParseException {
        PostDTO postDTO = postService.findPostDTOById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                postDTO));
    }
    @PostMapping(path = "/post")
    public ResponseEntity<?> savePost(@RequestBody PostDTO postDTO)  {

        Post post = postService.saveNewPost(postDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                post));
    }

    @PutMapping(path = "/post/upImg",consumes = MediaType.ALL_VALUE,
            produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> uPimg(@RequestParam(value = "img")MultipartFile file,
                                   @RequestParam Long postId) throws IOException {
        Post post = postService.findPostById(postId);
        if (!file.isEmpty()){
            Map params = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "postImages"
            );
            Map map = cloudinaryUpload.cloudinary().uploader().upload(Convert.convertMultiPartToFile(file),params);
            post.setImgUrl((String) map.get("secure_url"));
            postService.save(post);
//            return ResponseEntity.ok(map.get("secure_url"));
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                    map.get("secure_url")));
        }
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
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(false,"Delete failed"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ResponseDTO(false,e.getMessage(),null));

        }
    }
    @GetMapping("/post/user")
    public ResponseEntity<?> getAllPostUser(@RequestParam Long userId,
                                            @RequestParam(value = "page",defaultValue = "0") Integer page,
                                            @RequestParam(value = "size",defaultValue = "10") Integer size){
        List<PostDTO> posts;
        posts = postService.findPostOfUser(userId,page,size);

        return ResponseEntity.ok(new ResponseDTO(true,"Success",posts));
    }
    @GetMapping("/post/homepage")
    public ResponseEntity<?> getPostHomepage(@RequestParam Long userId,
                                             @RequestParam(value = "page",defaultValue = "0") Integer page,
                                             @RequestParam(value = "size",defaultValue = "10") Integer size){
        List<PostDTO> posts;
        posts = postService.findPostHomePage(userId, page, size);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",posts));
    }

    @PostMapping("/post/like")
    public ResponseEntity<?> likePost(@RequestParam Long postId,
                                      @RequestParam Long userId){
        postService.likePost(postId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",null));
    }





}
