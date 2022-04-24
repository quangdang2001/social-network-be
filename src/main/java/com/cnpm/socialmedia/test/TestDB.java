package com.cnpm.socialmedia.test;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.Date;

@SpringBootTest
public class TestDB {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    @Test
    public void test(){
        System.out.println(new Date());
    }
}
