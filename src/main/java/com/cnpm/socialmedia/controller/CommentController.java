package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.CmtDTO;
import com.cnpm.socialmedia.dto.CmtResponse;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.model.Comment;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.CommentService;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        Users users = comment.getUsers();

        CmtResponse cmtResponse = convertCmtToRes(users,comment);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                cmtResponse));
    }

    @GetMapping("/comment/post/child/{cmtParentId}")
    public ResponseEntity<?> getCmmentChild(@PathVariable Long cmtParentId){
        try {
            List<Comment> comments= commentService.findCommentChild(cmtParentId);
            List<CmtResponse> cmtResponses = new ArrayList<>();
            comments.forEach(comment -> {
                Users users = comment.getUsers();

                CmtResponse cmtResponse =convertCmtToRes(users,comment);
                cmtResponses.add(cmtResponse);
            });
            return  ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                    cmtResponses));
        }
        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                    e.getMessage()));
        }

    }
    @GetMapping("/comment/post")
    public ResponseEntity<?> getCommentPost(@RequestParam Long postId,
                                            @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "5") Integer size){
        Pageable pageRequest = PageRequest.of(page,size);
        List<Comment> comments = commentService.findCmtByPostId(postId,pageRequest);
        List<CmtResponse> cmtResponses = new ArrayList<>();
        comments.forEach(comment -> {
            Users users = comment.getUsers();

            CmtResponse cmtResponse =convertCmtToRes(users,comment);
            cmtResponses.add(cmtResponse);
        });

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                cmtResponses));
    }
//    @PostMapping("/comment/like")
//    public ResponseEntity<?> likeCmt(@RequestParam Long cmtId){
//        commentService.likeComment(cmtId);
//        return ResponseEntity.ok().build();
//    }
    @PostMapping("/comment/child")
    public ResponseEntity<?> cmtChild(@RequestBody CmtDTO cmtDTO){
        Comment comment = commentService.cmtComment(cmtDTO);
        Users users = comment.getUsers();
        CmtResponse cmtResponse = convertCmtToRes(users,comment);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                cmtResponse));
    }

    private CmtResponse convertCmtToRes(Users users, Comment comment){
        CmtResponse.User user = new CmtResponse.User(users.getId(),users.getFirstName(),users.getLastName(),
                users.getImageUrl());
        CmtResponse cmtResponse = CmtResponse.builder()
                .content(comment.getContent())
                .createTime(comment.getCreateTime())
                .cmtId(comment.getId())
                .userCmt(user)
                .build();
        if (comment.getCommentParrent()!=null){
            cmtResponse.setCmtParrentId(comment.getCommentParrent().getId());
        }
        return cmtResponse;
    }

}
