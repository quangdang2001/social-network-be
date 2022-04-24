package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.model.Comment;
import com.cnpm.socialmedia.repo.CommentRepo;
import com.cnpm.socialmedia.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceIplm implements CommentService {

    private final CommentRepo commentRepo;

    @Override
    public Comment findById(Long id) {
        return null;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepo.save(comment) ;
    }
}
