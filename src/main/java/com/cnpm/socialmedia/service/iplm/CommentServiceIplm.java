package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.CmtDTO;
import com.cnpm.socialmedia.model.Comment;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.CommentRepo;
import com.cnpm.socialmedia.repo.NotificationRepo;
import com.cnpm.socialmedia.service.CommentService;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceIplm implements CommentService {

    private final CommentRepo commentRepo;
    private final NotificationRepo notificationRepo;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Override
    public Comment findById(Long id) {
        Optional<Comment> comment = commentRepo.findById(id);
        return comment.orElse(null);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepo.save(comment) ;
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepo.deleteById(id);
    }

    @Override
    public List<Comment> findCommentsByUsers_Id(Long userId) {
        return commentRepo.findCommentsByUsers_Id(userId);
    }

    @Override
    public List<Comment> findCmtByPostId(Long postId, Pageable pageable) {
        return commentRepo.findCommentByPost_Id(postId,pageable);
    }

//    @Override
//    public void likeComment(Long cmtId) {
//        Comment comment = findById(cmtId);
//        comment.increaseLike();
//        save(comment);
//    }

    @Override
    public Comment cmtComment(CmtDTO cmtDTO) {
        Comment comment = new Comment();
        Comment cmtParent = findById(cmtDTO.getCmtId());

        comment.setContent(cmtDTO.getContent());
        comment.setCommentPost(false);
        comment.setPost(postService.findPostById(cmtDTO.getPostId()));
        comment.setCommentParrent(cmtParent);
        cmtParent.setCountReply(cmtParent.getCountReply()+1);
        save(cmtParent);
        save(comment);
        return comment;
    }

    @Override
    public Comment cmtPost(CmtDTO cmtDTO) {
        Comment comment = new Comment();
        Post post = postService.findPostById(cmtDTO.getPostId());
        Users users = userService.findById(cmtDTO.getUserId());
        comment.setContent(cmtDTO.getContent());
        comment.setCreateTime(new Date());
        comment.setPost(post);
        comment.setUsers(userService.findById(cmtDTO.getUserId()));

        String content = String.format("%s %s commented in your post.",users.getFirstName(),users.getLastName());

        Notification notification = new Notification();
        notification.setPost(post);
        notification.setContent(content);
        notification.setCreateTime(new Date());
        notification.setUserReceiver(post.getUsers());
        notification.setUserCreate(users);
        notificationRepo.save(notification);

        return comment;
    }
}
