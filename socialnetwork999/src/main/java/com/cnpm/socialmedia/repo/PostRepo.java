package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findAllByUsersOrderByCreateTimeDesc(Users user, Pageable pageable);
    List<Post> findAllByUsersInOrderByCreateTimeDesc(List<Users> usersId, Pageable pageable);
    List<Post> findAllByCountReportedGreaterThan(Integer num);

}
