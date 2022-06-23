package com.cnpm.socialmedia.test;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        System.out.println(userRepo.searchUserFirstLastName("quangg").size());
        System.out.println(userRepo.searchUserLastFirstName("quangg").size());

    }
    @Test
    public void testLazy(){
        Set<Users> users = userService.searchUser("dang1 quang1");
        users.forEach(user -> {
            System.out.println(user.getFirstName());
        });

    }
    @Test
    public void random(){
        System.out.println(RandomStringUtils.randomAlphanumeric(6));
    }
}
