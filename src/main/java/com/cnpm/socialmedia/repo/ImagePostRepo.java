package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagePostRepo extends JpaRepository<ImagePost,Long> {
}
