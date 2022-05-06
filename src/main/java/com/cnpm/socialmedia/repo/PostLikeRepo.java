package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepo extends JpaRepository<PostLike,Long> {
    PostLike findByPostId_IdAndUserId_Id(Long postId, Long userId);
}
