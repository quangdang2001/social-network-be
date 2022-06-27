package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.CmtDTO;
import com.cnpm.socialmedia.dto.CmtResponse;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.model.Comment;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.CommentService;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.utils.Convert;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> cmtPost(@RequestBody CmtDTO cmtDTO){
        CmtResponse cmtResponse = commentService.cmtPost(cmtDTO);

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

                CmtResponse cmtResponse =Convert.convertCmtToRes(users,comment);
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

            CmtResponse cmtResponse =Convert.convertCmtToRes(users,comment);
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
    public ResponseEntity<?> cmtChild(@RequestBody CmtDTO cmtDTO) throws InterruptedException {
        CmtResponse cmtResponse = commentService.cmtComment(cmtDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true,"Success",
                cmtResponse));
    }

    @DeleteMapping("/comment/{cmtId}")
    public ResponseEntity<?> deleteCmt(@PathVariable Long cmtId){
        commentService.deleteCommentById(cmtId);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }


}
