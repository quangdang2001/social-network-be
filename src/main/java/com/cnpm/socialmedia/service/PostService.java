package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.model.Post;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostService {

    //post
    Post save(Post post);
    List<PostDTO> findPostHomePage(Long userId, Pageable pageable);
    List<Post> findPostOfUser(Long userId, Pageable pageable);
    Post findPostById(Long id);
    void deletePostById(Long id);

}
