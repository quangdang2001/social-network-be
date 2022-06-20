package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.ModelRegister.VerificationToken;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.repo.VerificationTokenRepo;
import com.cnpm.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceIplm implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final VerificationTokenRepo verificationTokenRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Users findById(Long id) {
        Optional<Users> users = userRepo.findById(id);
        return users.orElse(null);
    }

    @Override
    public Users saveRegister(UserDTO userDTO) {
        Users user;
        user = findUserByEmail(userDTO.getEmail());
        if (user!=null){
            return null;
        }
        user = new Users();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setBio(userDTO.getBio());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setBirthDay(userDTO.getBirthDay());
        return userRepo.save(user);
    }

    @Override
    public Users save(Users users) {
        return userRepo.save(users);
    }

    @Override
    public Users findUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    @Override
    public void saveVerificationTokenForUser(String token, Users user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String email, String token) {
        VerificationToken verificationToken
                = verificationTokenRepo.findVerificationTokenByUserEmailAndToken(email,token);

        if (verificationToken == null) {
            return "invalid";
        }

        Users user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepo.delete(verificationToken);
            return "expired";
        }

        user.setEnable(true);
        userRepo.save(user);
        return "valid";
    }

    @Override
    public VerificationToken SendToken(String email) {
        VerificationToken verificationToken = verificationTokenRepo.findVerificationTokenByUserEmail(email);
        if (verificationToken != null){
            String token = UUID.randomUUID().toString();
            verificationToken.setToken(token);
            verificationTokenRepo.save(verificationToken);
            return verificationToken;
        }
        return null;
    }



    @Override
    public String validatePasswordResetToken(String email, String token) {

        VerificationToken verificationToken
                = verificationTokenRepo.findVerificationTokenByUserEmailAndToken(email,token);

        if (verificationToken == null) {
            return "invalid";
        }

        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepo.delete(verificationToken);
            return "expired";
        }

        return "valid";
    }

    @Override
    public void changePassword(Users user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(Users user, String oldPassword) {
        return passwordEncoder.matches(oldPassword,user.getPassword());
    }

    @Override
    public Boolean reportUser(Long userId) {
        Users users = findById(userId);
        if (users!= null){
            users.setCountReport(users.getCountReport()+1);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = findUserByEmail(username);
        if (user != null) {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(user.getId().toString(), user.getPassword(), authorities);
        }
        return null;
    }
}
