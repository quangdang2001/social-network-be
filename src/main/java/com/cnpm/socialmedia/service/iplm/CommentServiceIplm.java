package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.model.Comment;
import com.cnpm.socialmedia.repo.CommentRepo;
import com.cnpm.socialmedia.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceIplm implements CommentService {

    private final CommentRepo commentRepo;

    @Override
    public Comment findById(Long id) {
        Optional<Comment> comment = commentRepo.findById(id);
        return comment.orElse(null);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepo.save(comment) ;
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepo.deleteById(id);
    }

    @Override
    public List<Comment> findCommentsByUsers_Id(Long userId) {
        return commentRepo.findCommentsByUsers_Id(userId);
    }

    @Override
    public List<Comment> findCmtByPostId(Long postId, Pageable pageable) {
        return commentRepo.findCommentByPost_Id(postId,pageable);
    }
}
