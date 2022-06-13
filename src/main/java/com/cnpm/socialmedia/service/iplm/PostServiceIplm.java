package com.cnpm.socialmedia.service.iplm;

import com.cloudinary.utils.ObjectUtils;
import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.PostLike;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.NotificationRepo;
import com.cnpm.socialmedia.repo.PostLikeRepo;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.repo.UserFollowingRepo;
import com.cnpm.socialmedia.service.Cloudinary.CloudinaryUpload;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserFollowingService;
import com.cnpm.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceIplm implements PostService {

    private final PostRepo postRepo;
    private final PostLikeRepo postLikeRepo;
    private final NotificationRepo notificationRepo;

    @Autowired
    private UserFollowingService userFollowingService;
    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryUpload cloudinaryUpload;

    @Override
    public Post save(Post post) {
        return postRepo.save(post);
    }

    @Override
    public List<PostDTO> findPostHomePage(Long userId, Integer page, Integer size) {
        List<Long> usersFollowingId = userFollowingService.findAllIdFollowingUser(userId);
        Pageable pageable = PageRequest.of(page,size);
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

            boolean check = postLikeRepo.findByPostId_IdAndUserId_Id(post.getId(),userId) != null;
            postDTO.setLiked(check);

            postDTO.setUserDTO(userDTO);
            postDTOS.add(postDTO);
        });
        return postDTOS;
    }

    @Override
    public List<PostDTO> findPostOfUser(Long userId, Integer page, Integer size) {
        List<Post> posts;
        Pageable pageable = PageRequest.of(page,size);
        posts = postRepo.findAllByUsers_IdOrderByCreateTimeDesc(userId,pageable);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(posts.get(0).getUsers().getId());
        userDTO.setFirstName(posts.get(0).getUsers().getFirstName());
        userDTO.setLastName(posts.get(0).getUsers().getLastName());
        userDTO.setEmail(posts.get(0).getUsers().getEmail());
        userDTO.setImageUrl(posts.get(0).getUsers().getImageUrl());

        List<PostDTO> postDTOS = new ArrayList<>();

        posts.forEach(post -> {
            PostDTO postDTO = new PostDTO(post.getId(),post.getContent(),post.getImgUrl(),post.getUsers().getId(),
                    post.getCountLiked(),post.getCountCmted(),post.getCountShated(),post.getCountReported(),
                    post.getCreateTime(),post.getUpdateTime(),post.isPostShare(),
                    post.getPostShared() == null ? null:post.getPostShared().getId());

            boolean check = postLikeRepo.findByPostId_IdAndUserId_Id(post.getId(),userId) != null;
            postDTO.setLiked(check);

            postDTO.setUserDTO(userDTO);
            postDTOS.add(postDTO);
        });

        return postDTOS;
    }

    @Override
    public Notification likePost(Long postId, Long userId) {
        boolean check = postLikeRepo.findByPostId_IdAndUserId_Id(postId,userId) != null;
        Post post = findPostById(postId);
        PostLike postLike = new PostLike(post, userService.findById(userId));
        if (!check) {
            post.increaseLike();
            postLikeRepo.save(postLike);
            save(post);
            Notification notification = new Notification();
            Users users = userService.findById(userId);
            String content = String.format("%s %s liked your post.", users.getLastName(), users.getFirstName());
            notification.setContent(content);
            notification.setUserReceiver(post.getUsers());
            notification.setUserCreate(users);
            notification.setPost(post);
            notification.setCreateTime(new Date());

            notificationRepo.save(notification);
            return notification;

        }else {
            post.decreaseLike();
            postLikeRepo.delete(postLike);
            return null;
        }
    }

    @Override
    public Post findPostById(Long id) {
        Optional<Post> post = postRepo.findById(id);
        return post.orElse(null);
    }

    @Override
    public void deletePostById(Long id) throws IOException {
        Post post = findPostById(id);
        if (post.getImgUrl()!= null) {
            cloudinaryUpload.cloudinary().uploader().destroy("postImages/" + cloudinaryUpload.getPublicId(post.getImgUrl()),
                    ObjectUtils.asMap("resource_type", "image"));
        }
        postRepo.deleteById(id);
    }

    @Override
    public PostDTO findPostDTOById(Long id) {
        Post post = findPostById(id);
        PostDTO postDTO = new PostDTO();
        postDTO.setUserId(post.getUsers().getId());
        postDTO.setId(post.getId());
        postDTO.setContent(post.getContent());
        postDTO.setUrlImage(post.getImgUrl());
        postDTO.setCountCmted(post.getCountCmted());
        postDTO.setCountLiked(post.getCountLiked());
        postDTO.setCountShated(post.getCountShated());
        postDTO.setCountReported(post.getCountReported());
        postDTO.setCreateTime(post.getCreateTime());
        postDTO.setUpdateTime(post.getUpdateTime());
        return postDTO;
    }

    @Override
    public Post saveNewPost(PostDTO postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setImgUrl(postDTO.getUrlImage());
        post.setUsers(userService.findById(postDTO.getUserId()));
        post.setCreateTime(new Date());
        post.setPostShared(null);
        save(post);
        return post;
    }

    @Override
    public Post updatePost(PostDTO postDTO) {
        Post post = findPostById(postDTO.getId());
        post.setContent(postDTO.getContent());
        post.setImgUrl(post.getImgUrl());
        post.setUpdateTime(new Date());
        save(post);
        return post;
    }




}
