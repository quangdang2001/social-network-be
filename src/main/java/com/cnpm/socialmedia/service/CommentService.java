package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.dto.CmtDTO;
import com.cnpm.socialmedia.model.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Comment findById(Long id);
    Comment save(Comment comment);
    void deleteCommentById(Long id);
    List<Comment> findCommentsByUsers_Id(Long userId);
    List<Comment> findCmtByPostId(Long postId, Pageable pageable);
    /////
//    void likeComment(Long cmtId);
    Comment cmtComment(CmtDTO cmtDTO);
    Comment cmtPost(CmtDTO cmtDTO);
    List<Comment> findCommentChild(Long cmtIdParent);

}
