package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.CmtDTO;
import com.cnpm.socialmedia.model.Comment;
import com.cnpm.socialmedia.service.CommentService;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> cmtPost(@RequestBody CmtDTO cmtDTO){
        Comment comment = commentService.cmtPost(cmtDTO);
        return ResponseEntity.ok(comment);
    }
    @GetMapping("/comment/post")
    public ResponseEntity<?> getCommentPost(@RequestParam Long postId,
                                            @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "5") Integer size){
        Pageable pageRequest = PageRequest.of(page,size);
        List<Comment> comments = commentService.findCmtByPostId(postId,pageRequest);
        return ResponseEntity.ok(comments);
    }
    @PostMapping("/comment/like")
    public ResponseEntity<?> likeCmt(@RequestParam Long cmtId){
        commentService.likeComment(cmtId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/comment/child")
    public ResponseEntity<?> cmtChild(@RequestBody CmtDTO cmtDTO){
        Comment comment = commentService.cmtComment(cmtDTO);
        return ResponseEntity.ok(comment);
    }

}
