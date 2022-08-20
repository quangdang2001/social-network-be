package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Users findUserByEmail(String email);
    Boolean existsByEmail(String email);
    List<Users> findAllByCountReportGreaterThan(Integer num);
    List<Users> findAllByIdIn(List<Long> userIds);

    @Query("select u from Users u where u.email like concat('%',:email,'%')")
    List<Users> searchByEmail(String email);

    @Query("SELECT u from Users u where concat('%',lower(u.lastName),' ',lower(u.firstName) ,'%') like concat('%',:keyword,'%')")
    List<Users> searchUserLastFirstName(String keyword);

    @Query("SELECT u from Users u where concat('%',lower(u.firstName) ,' ',lower(u.lastName) ,'%') like concat('%',:keyword,'%')")
    List<Users> searchUserFirstLastName(String keyword);

    @Query("select u from Users u order by u.countFollower desc")
    List<Users> findTop10Follower(Pageable pageable);

}
