package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.ImagePost;
import com.cnpm.socialmedia.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagePostRepo extends JpaRepository<ImagePost,Long> {
    List<ImagePost> findAllByPost(Post post);
}
