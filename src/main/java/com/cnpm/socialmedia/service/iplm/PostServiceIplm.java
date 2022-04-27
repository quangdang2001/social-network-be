package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.repo.UserFollowingRepo;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserFollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceIplm implements PostService {

    private final PostRepo postRepo;
    private final UserFollowingRepo userFollowingRepo;
    @Autowired
    private UserFollowingService userFollowingService;
    @Override
    public Post save(Post post) {
        return postRepo.save(post);
    }

    @Override
    public List<PostDTO> findPostHomePage(Long userId, Pageable pageable) {
        List<Long> usersFollowingId = userFollowingService.findAllIdFollowingUser(userId);
        List<Post> posts =postRepo.findAllByUsers_IdInOrderByCreateTimeDesc(usersFollowingId,pageable);
        List<PostDTO> postDTOS = new ArrayList<>();
        posts.forEach(post -> {
            PostDTO postDTO = new PostDTO(post.getId(),post.getContent(),post.getImgUrl(),post.getUsers().getId(),
                    post.getCountLiked(),post.getCountCmted(),post.getCountShated(),post.getCountReported(),
                    post.getCreateTime(),post.getUpdateTime(),post.isPostShare(),
                    post.getPostShared() == null ? null:post.getPostShared().getId());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(post.getUsers().getId());
            userDTO.setFirstName(post.getUsers().getFirstName());
            userDTO.setLastName(post.getUsers().getLastName());
            userDTO.setEmail(post.getUsers().getEmail());
            userDTO.setImageUrl(post.getUsers().getImageUrl());
            postDTO.setUserDTO(userDTO);
            postDTOS.add(postDTO);
        });
        return postDTOS;
    }

    @Override
    public List<Post> findPostOfUser(Long userId, Pageable pageable) {
        return postRepo.findAllByUsers_IdOrderByCreateTimeDesc(userId,pageable);
    }

    @Override
    public Post findPostById(Long id) {
        Optional<Post> post = postRepo.findById(id);
        return post.orElse(null);
    }

    @Override
    public void deletePostById(Long id) {
        postRepo.deleteById(id);
    }


}
