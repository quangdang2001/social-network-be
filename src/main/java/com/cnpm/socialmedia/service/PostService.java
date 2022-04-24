package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.model.Post;

import java.util.List;

public interface PostService {
    Post save(Post post);
    List<Post> findPostHomePage(Long userId);
    List<Post> findPostOfUser(Long userId);
    Post findPostById(Long id);
    void deletePostById(Long id);
}
