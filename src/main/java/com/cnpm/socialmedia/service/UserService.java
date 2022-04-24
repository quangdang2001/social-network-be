package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.ModelRegister.VerificationToken;
import com.cnpm.socialmedia.model.Users;

public interface UserService {
    Users findById(Long id);
    Users save(UserDTO userDTO);
    Users findUserByEmail(String email);
    void saveVerificationTokenForUser(String token,Users user);

    String validateVerificationToken(String email,String token);

    VerificationToken SendToken(String email);



    String validatePasswordResetToken(String email, String token);
    void changePassword(Users user, String newPassword);

    boolean checkIfValidOldPassword(Users user, String oldPassword);
}
