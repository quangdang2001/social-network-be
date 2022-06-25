package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {
    List<Comment> findCommentsByUsers_Id(Long id);
    List<Comment> findCommentByPost_IdOrderByCreateTimeDesc(Long id, Pageable pageable);
    List<Comment> findCommentByCommentParrent_IdOrderByCreateTimeDesc(Long id);
}
