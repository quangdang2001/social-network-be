package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.CmtDTO;
import com.cnpm.socialmedia.model.Comment;
import com.cnpm.socialmedia.model.Notification;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.CommentRepo;
import com.cnpm.socialmedia.repo.NotificationRepo;
import com.cnpm.socialmedia.service.CommentService;
import com.cnpm.socialmedia.service.NotificationService;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceIplm implements CommentService {

    private final CommentRepo commentRepo;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;

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
        Post post = postService.findPostById(cmtDTO.getPostId());
        Users users = userService.findById(cmtDTO.getUserId());
        comment.setContent(cmtDTO.getContent());
        comment.setCommentPost(false);
        comment.setUsers(users);
        comment.setPost(post);
        comment.setCreateTime(new Date());
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
        comment.setUsers(users);
        commentRepo.save(comment);
        String content = String.format("%s %s commented in your post.",users.getFirstName(),users.getLastName());
        notificationService.sendNotificationPost(post,users,content);

        return comment;
    }

    @Override
    public List<Comment> findCommentChild(Long cmtIdParent) {
        List<Comment> comments = commentRepo.findCommentByCommentParrent_Id(cmtIdParent);
        return comments;
    }
}
