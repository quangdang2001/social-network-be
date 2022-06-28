package com.cnpm.socialmedia.service.iplm;

import com.cloudinary.utils.ObjectUtils;
import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.PostDTO;
import com.cnpm.socialmedia.dto.PostShareDTO;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.*;
import com.cnpm.socialmedia.repo.ImagePostRepo;
import com.cnpm.socialmedia.repo.PostLikeRepo;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.service.Cloudinary.CloudinaryUpload;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserFollowingService;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.utils.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceIplm implements PostService {

    private final PostRepo postRepo;
    private final PostLikeRepo postLikeRepo;
    private final NotificationService notificationService;
    private final UserFollowingService userFollowingService;
    private final UserService userService;
    private final CloudinaryUpload cloudinaryUpload;
    private final ImagePostRepo imagePostRepo;

    @Override
    public Post save(Post post) {
        return postRepo.save(post);
    }

    @Override
    public List<PostDTO> findPostHomePage(Long userId, Integer page, Integer size) {
        List<Users> usersFollowingId = userFollowingService.findAllIdFollowingUser(userId);
        Users users = userService.findById(userId);
        usersFollowingId.add(users);
        Pageable pageable = PageRequest.of(page,size);
        List<Post> posts =postRepo.findAllByUsersInOrderByCreateTimeDesc(usersFollowingId,pageable);

        return  convertPostsToPostDTOs(posts,users);
    }

    @Override
    public List<PostDTO> findPostOfUser(Long userId, Integer page, Integer size) {
        List<Post> posts;
        Pageable pageable = PageRequest.of(page,size);
        Users users = userService.findById(userId);
        posts = postRepo.findAllByUsersOrderByCreateTimeDesc(users,pageable);


        return convertPostsToPostDTOs(posts,users);
    }

    @Override
    public NotificationPayload likePost(Long postId, Long userId){
        Post post = findPostById(postId);
        Users users = userService.findById(userId);
        NotificationPayload notificationPayload = null;
        if (post!=null && users!=null) {
            boolean check = postLikeRepo.findByPostIdAndUserId(post, users) != null;

            if (!check) {
                PostLike postLike = new PostLike(post,users);
                post.increaseLike();
                postLikeRepo.save(postLike);
                save(post);

                if (!post.getUsers().getId().equals(userId)) {
                    String content = String.format("%s %s liked your post.", users.getLastName(), users.getFirstName());
                    notificationPayload =
                            Convert.convertNotificationToNotifiPayload(notificationService.sendNotificationPost(post,users,content));
                }

            } else {
                PostLike postLike = new PostLike(post,users);
                post.decreaseLike();
                postLikeRepo.delete(postLike);
            }
            return notificationPayload;
        }
        return null;
    }

    @Override
    public Boolean reportPost(Long postId) {
        Post post = findPostById(postId);
        if (post!=null){
            post.setCountReported(post.getCountReported()+1);
            if (post.getCountReported()>50){
                postRepo.deleteById(postId);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Post> findPostReported() {
        List<Post> posts = postRepo.findAllByCountReportedGreaterThan(20);
        return posts;
    }

    @Override
    public NotificationPayload sharePost(PostDTO postDTO){
        Post post = new Post();
        Users userCreate = userService.findById(postDTO.getUserId());
        if (userCreate.isEnable()) {
            Post postParent = findPostById(postDTO.getPostSharedId());
            postParent.setCountShated(post.getCountShated() + 1);
            post.setPostShare(true);
            post.setContent(postDTO.getContent());
            post.setUsers(userCreate);
            post.setPostShared(postParent);
            post.setCreateTime(new Date());

            save(post);
            save(postParent);
            Users users = post.getUsers();
            if (!post.getPostShared().getUsers().getId().equals(postDTO.getUserId())) {
                String content = String.format("%s %s shared your post.", users.getLastName(), users.getFirstName());
                Notification notification = notificationService.sendNotificationPost(post.getPostShared(), userCreate, content);
                notificationService.save(notification);
                return Convert.convertNotificationToNotifiPayload(notification);
            }
        }
        return null;
    }

    @Override
    public String upImagePost(MultipartFile file, Long postId) throws IOException {
        Post post = findPostById(postId);
        if (!file.isEmpty()){
            Map params = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "postImages"
            );
            Map map = cloudinaryUpload.cloudinary().uploader().upload(Convert.convertMultiPartToFile(file),params);
            ImagePost imagePost = new ImagePost();
            imagePost.setPost(post);
            imagePost.setUrlImage((String) map.get("secure_url"));

            imagePostRepo.save(imagePost);
            return map.get("secure_url").toString();
        }
        return null;
    }

    @Override
    public Post findPostById(Long id) {
        Optional<Post> post = postRepo.findById(id);
        return post.orElse(null);
    }

    @Override
    public void deletePostById(Long id) throws IOException {
        Post post = findPostById(id);
        List<ImagePost> images = post.getImages();
        if (images!= null) {
            images.forEach(image ->{
                try {
                    cloudinaryUpload.cloudinary().uploader().destroy("postImages/" + cloudinaryUpload.getPublicId(image.getUrlImage()),
                            ObjectUtils.asMap("resource_type", "image"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        postRepo.deleteById(id);
    }

    @Override
    public PostDTO findPostDTOById(Long id,Long userId) {
        Post post = findPostById(id);
        Users users = userService.findById(userId);
        return convertPostToPostDTO(post,users);
    }

    @Override
    public Post saveNewPost(PostDTO postDTO) {
        Users users = userService.findById(postDTO.getUserId());
        Post post = null;
        if (users.isEnable()) {
            post = new Post();
            post.setContent(postDTO.getContent());
            post.setUsers(users);
            post.setPostShared(null);
            save(post);
        }
        return post;
    }

    @Override
    public Post updatePost(PostDTO postDTO) {
        Post post = findPostById(postDTO.getId());
        post.setContent(postDTO.getContent());
        save(post);
        return post;
    }

    private List<PostDTO> convertPostsToPostDTOs(List<Post> posts, Users user){
        List<PostDTO> postDTOS = new ArrayList<>();
        posts.forEach(post -> {
            postDTOS.add(convertPostToPostDTO(post,user));
        });
        return postDTOS;
    }

    private PostDTO convertPostToPostDTO(Post post,Users user){
        PostDTO postDTO = new PostDTO(post.getId(),post.getContent(),post.getUsers().getId(),
                post.getCountLiked(),post.getCountCmted(),post.getCountShated(),post.getCountReported(),
                post.getCreateTime(),post.getUpdateTime(),post.isPostShare());

        if (post.isPostShare()) {
            PostShareDTO postShareDTO = new PostShareDTO();
            Post postShare = post.getPostShared();
            Users userCreate = postShare.getUsers();
            postShareDTO.setPostShared(postShare);
            postShareDTO.setUserCreateId(userCreate.getId());
            postShareDTO.setFirstName(userCreate.getFirstName());
            postShareDTO.setLastName(userCreate.getLastName());
            postShareDTO.setAvatar(userCreate.getImageUrl());
            postDTO.setPostShared(postShareDTO);
        }
        postDTO.setImages(post.getImages());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(post.getUsers().getId());
        userDTO.setFirstName(post.getUsers().getFirstName());
        userDTO.setLastName(post.getUsers().getLastName());
        userDTO.setEmail(post.getUsers().getEmail());
        userDTO.setImageUrl(post.getUsers().getImageUrl());
        if (user!=null) {
            boolean check = postLikeRepo.findByPostIdAndUserId(post, user) != null;
            postDTO.setLiked(check);
        }
        postDTO.setUserCreate(userDTO);
        return postDTO;
    }


}
