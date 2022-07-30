package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.dto.PostReq;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.Post;
import net.minidev.json.JSONArray;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post save(Post post);
    Post findPostById(Long id);
    void deletePostById(Long id) throws IOException;

    /////
    PostDTO findPostDTOById(Long id);
    Post saveNewPost(PostReq postReq);
    Post updatePost(PostDTO postDTO);
    List<PostDTO> findPostHomePage(Integer page, Integer size);
    List<PostDTO> findPostOfUser(Long userId, Integer page, Integer size);
    NotificationPayload likePost(Long postId);
    Boolean reportPost(Long postId);
    List<Post> findPostReported();
    NotificationPayload sharePost(PostDTO postDTO);
    String upImagePost(MultipartFile file, Long postId) throws IOException;
    JSONArray getPostAdmin(int page, int size);
}
