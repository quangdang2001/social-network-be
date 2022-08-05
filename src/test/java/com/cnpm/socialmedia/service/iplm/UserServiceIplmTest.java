package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.CmtResponse;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.repo.VerificationTokenRepo;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceIplmTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private VerificationTokenRepo verificationTokenRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    UserServiceIplm userService;



    @Test
    void saveRegister() {
        String email = "quadnag@gmail.com";

        Users users = new Users(
                null,"Quang","Dang",null, email,null,0,null,null,null,
                0,0,0,false, null,null,null,null,null,null,null,null
        );
        UserDTO userDTO = new UserDTO(
                null,"Quang","Dang", email,"ii"
        );
        userService.saveRegister(userDTO);

        ArgumentCaptor<Users> argumentCaptor = ArgumentCaptor.forClass(Users.class);
        verify(userRepo).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(users);
    }

    @Test
    @Disabled
    void save() {
    }

    @Test
    void findUserByEmail() {
        userService.findUserByEmail("Email");
        verify(userRepo).findUserByEmail("Email");
    }
}