package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Users findUserByEmail(String email);
    List<Users> findAllByCountReportGreaterThan(Integer num);

    @Query("SELECT u from Users u where concat('%',u.lastName,' ',u.firstName,'%') like concat('%',:keyword,'%')")
    List<Users> searchUserLastFirstName(String keyword);

    @Query("SELECT u from Users u where concat('%',u.firstName,' ',u.lastName,'%') like concat('%',:keyword,'%')")
    List<Users> searchUserFirstLastName(String keyword);

}
