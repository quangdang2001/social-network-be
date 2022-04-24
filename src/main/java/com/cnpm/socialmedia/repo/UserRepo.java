package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Users findUserByEmail(String email);
}
