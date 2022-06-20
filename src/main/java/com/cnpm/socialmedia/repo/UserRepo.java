package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Users findUserByEmail(String email);
    List<Users> findAllByCountReportGreaterThan(Integer num);
    List<Users> findAllByFirstNameContaining(String firstName);
    List<Users> findAllByLastNameContaining(String lastName);
    List<Users> findAllByEmailContaining(String email);

    List<Users> findAllByFirstNameLike(String firstName);
    List<Users> findAllByLastNameLike(String lastName);
    List<Users> findAllByEmailLike(String email);
}
