package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.model.ModelRegister.VerificationToken;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.repo.VerificationTokenRepo;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.utils.Constant;
import com.cnpm.socialmedia.utils.Convert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@Slf4j
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
        user.setFirstName(Convert.formatName(userDTO.getFirstName()));
        user.setLastName(Convert.formatName(userDTO.getLastName()));
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
                = verificationTokenRepo.findVerificationTokenByToken(token);

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
        verificationToken.setToken(null);
        verificationTokenRepo.save(verificationToken);
        user.setEnable(true);
        user.setRole(Constant.ROLE_USER);
        userRepo.save(user);
        return "valid";
    }

    @Override
    public VerificationToken SendToken(String email) {
        VerificationToken verificationToken = verificationTokenRepo.findVerificationTokenByUserEmail(email);
        if (verificationToken != null){
            String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
            verificationToken.setToken(token);
            verificationTokenRepo.save(verificationToken);
            return verificationToken;
        }
        return null;
    }



    @Override
    public Users validatePasswordResetToken(String token) {

        VerificationToken verificationToken
                = verificationTokenRepo.findVerificationTokenByToken(token);

        if (verificationToken == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepo.delete(verificationToken);
            return null;
        }
        Users users = verificationToken.getUser();
        verificationToken.setToken(null);
        verificationTokenRepo.save(verificationToken);
        return users;
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
    public List<Users> findUserReported() {
        return userRepo.findAllByCountReportGreaterThan(20);
    }

    @Override
    public Set<Users> searchUser(String keyword) {
        keyword = keyword.trim();
        keyword = keyword.replaceAll("  ", " ");
        System.out.println(keyword);
        Set<Users> users= new HashSet<>();
        if (keyword.contains("gmail")){
            keyword = keyword.substring(0,keyword.indexOf("@"));

            users.addAll(userRepo.searchByEmail(keyword));
        }
        else {
            users.addAll(userRepo.searchUserFirstLastName(keyword));
            users.addAll(userRepo.searchUserLastFirstName(keyword));
        }
        return users;
    }

    @Override
    public Users updateUser(UserDTO userDTO) {
        Users users = findById(userDTO.getId());
        if (users!=null){
            if (userDTO.getFirstName()!=null && !userDTO.getFirstName().trim().equals("") ){
                users.setFirstName(Convert.formatName(userDTO.getFirstName()));
            }
            if (userDTO.getLastName()!=null && !userDTO.getLastName().trim().equals("") ){
                users.setLastName(Convert.formatName(userDTO.getLastName()));
            }
            if ( userDTO.getAddress()!=null && !userDTO.getAddress().trim().equals("")){
                users.setAddress(Convert.formatName(userDTO.getAddress()));
            }
            if ( userDTO.getBio()!=null && !userDTO.getBio().trim().equals("")){
                users.setBio(Convert.formatName(userDTO.getBio()));
            }
            if ( userDTO.getGender()==1 && userDTO.getGender()==0){
                users.setGender(userDTO.getGender());
            }
            if ( userDTO.getBirthDay()!=null && !userDTO.getBirthDay().equals("")){
                users.setBirthDay(userDTO.getBirthDay());
            }
        }
        save(users);
        return users;
    }

    @Override
    public Page<Users> getUserAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Users> users = userRepo.findAll(pageable);
        return  users;
    }

    @Override
    public boolean deleteUser(Long userId) {
        try {
            Users users = findById(userId);
            users.setRole(null);
            users.setEnable(false);
            userRepo.save(users);
            return true;
        }catch (Exception e){
            log.error("User delete exception: ",e.getMessage());
            return false;
        }
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
