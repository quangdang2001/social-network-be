package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.PostLike;
import com.cnpm.socialmedia.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepo extends JpaRepository<PostLike,Long> {
    PostLike findByPostIdAndUserId(Post postId, Users userId);

    Boolean existsByPostIdAndUserId_Id(Post postId, Long userId);
}
