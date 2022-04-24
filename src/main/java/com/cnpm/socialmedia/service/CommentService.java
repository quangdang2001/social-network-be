package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.model.Comment;

public interface CommentService {
    Comment findById(Long id);
    Comment save(Comment comment);

}
