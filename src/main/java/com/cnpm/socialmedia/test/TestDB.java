package com.cnpm.socialmedia.test;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Service
public class TestDB {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PostRepo postRepo;


    @Test
    public void test(){
        System.out.println(new Date());
    }
    @Test

    public void testLazy(){
        List<Users> users = userRepo.findAllByFirstNameContaining("quang dang");
        users.forEach(user -> {
            System.out.println(user.getFirstName());
        });

    }
}
