package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }
//
//    @Test
//    public void shouldSaveUser(){
//        Users users = new Users(null,"Quang","Dang",null,"quandang@gmail.com",
//                "123",1,"null","null",null,0,0,0,true,"ROLE_ADMIN",null,
//                null,null,null,null,null,null);
//        Users userSave = userRepo.save(users);
//
//        assertThat(userSave).usingRecursiveComparison().ignoringFields("id").isEqualTo(users);
//    }
//    @Test
//    public void findByName(){
//        Users users = new Users(null,"Quang","Dang",null,"quandang@gmail.com",
//                "123",1,"null","null",null,0,0,0,true,"ROLE_ADMIN",null,
//                null,null,null,null,null,null);
//        userRepo.save(users);
//        Users userSave = userRepo.findUserByEmail("quandang@gmail.com");
//        assertThat(userSave).isNotNull();
//    }

}
