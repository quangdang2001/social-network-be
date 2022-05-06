package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.Post;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post save(Post post);
    Post findPostById(Long id);
    void deletePostById(Long id) throws IOException;

    /////
    PostDTO findPostDTOById(Long id);
    Post saveNewPost(PostDTO postDTO);
    Post updatePost(PostDTO postDTO);
    List<PostDTO> findPostHomePage(Long userId, Integer page, Integer size);
    List<PostDTO> findPostOfUser(Long userId, Integer page, Integer size);
    Notification likePost(Long postId, Long userId);
}
