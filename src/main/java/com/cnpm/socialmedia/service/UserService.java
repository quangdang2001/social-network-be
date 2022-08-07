package com.cnpm.socialmedia.service;

import com.cnpm.socialmedia.controller.ws.Payload.NotificationPayload;
import com.cnpm.socialmedia.dto.LoginRequest;
import com.cnpm.socialmedia.dto.PersonalPage;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.ModelRegister.VerificationToken;
import com.cnpm.socialmedia.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface UserService {
    Users findById(Long id);
    Users saveRegister(UserDTO userDTO);
    Users save(Users users);
    Users findUserByEmail(String email);
    void saveVerificationTokenForUser(String token,Users user);
    String validateVerificationToken(String email,String token);
    VerificationToken SendToken(String email);
    Users validatePasswordResetToken(String token);
    void changePassword(Users user, String newPassword);
    boolean checkIfValidOldPassword(Users user, String oldPassword);
    Boolean reportUser(Long userId);
    List<Users> findUserReported();
    Set<Users> searchUser(String keyword);
    Users updateUser(UserDTO userDTO);
    Page<Users> getUserAdmin(int page, int size);
    boolean deleteUser(Long userId);
    List<Users> findAllUsersByListUserId(List<Long> id);
    boolean enableUser(String email);

    NotificationPayload followUser(Long userFollowedId);

    PersonalPage seePersonalPage(Long personalPageId);

    String upImageProfile(MultipartFile file) throws IOException;

    Object login(LoginRequest loginRequest, HttpServletRequest request);
}
