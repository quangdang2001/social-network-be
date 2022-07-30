package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.PostReq;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.repo.ImagePostRepo;
import com.cnpm.socialmedia.repo.PostLikeRepo;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.Cloudinary.CloudinaryUpload;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserFollowingService;
import com.cnpm.socialmedia.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceIplmTest {

    @Mock
    private PostRepo postRepo;
    @Mock
    private PostLikeRepo postLikeRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserFollowingService userFollowingService;
    @Mock
    private UserService userService;
    @Mock
    private CloudinaryUpload cloudinaryUpload;
    @Mock
    private ImagePostRepo imagePostRepo;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    void saveNewPost() {
        PostService postService = new PostServiceIplm(postRepo,postLikeRepo,userRepo,notificationService,userFollowingService,userService,cloudinaryUpload,
                imagePostRepo,simpMessagingTemplate);
        Post post = new Post();
        post.setContent("AAA");

    }
}