package com.cnpm.socialmedia.test;
import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.MessageRepo;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.PostService;
import com.cnpm.socialmedia.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private MessageRepo messageRepo;


    @Test
    public void test(){
        Pageable pageable = PageRequest.of(0,10);
        System.out.println(messageRepo.findUserChat(Long.parseLong("1"),pageable));

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
