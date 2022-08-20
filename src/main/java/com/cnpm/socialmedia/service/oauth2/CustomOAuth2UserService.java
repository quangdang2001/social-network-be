package com.cnpm.socialmedia.service.oauth2;

import com.cnpm.socialmedia.exception.OAuth2AuthenticationProcessingException;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.auth.UserDetailIplm;
import com.cnpm.socialmedia.service.oauth2.user.OAuth2UserInfo;
import com.cnpm.socialmedia.service.oauth2.user.OAuth2UserInfoFactory;
import com.cnpm.socialmedia.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepo usersRepo;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(),oAuth2User.getAttributes());

        Users users = usersRepo.findUserByEmail(oAuth2UserInfo.getEmail());
        if (users!=null){
            if (users.getProvider() == null || !users.getProvider().equals(userRequest.getClientRegistration().getRegistrationId())){
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        users.getProvider() + " account. Please use your " + users.getProvider() +
                        " account to login.");
            }
            users.setFirstName(oAuth2UserInfo.getName().substring(0,oAuth2UserInfo.getName().indexOf(" ")));
            users.setLastName(oAuth2UserInfo.getName().substring(oAuth2UserInfo.getName().indexOf(" ")+1));
            users.setImageUrl(oAuth2UserInfo.getImageUrl());
            usersRepo.save(users);
        }
        else {
          users = registerNewUser(userRequest,oAuth2UserInfo);
        }

        return UserDetailIplm.create(users,oAuth2UserInfo.getAttributes());
    }
    private Users registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Users user = new Users();

        user.setProvider(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        user.setFirstName(oAuth2UserInfo.getName().substring(0,oAuth2UserInfo.getName().indexOf(" ")));
        user.setLastName(oAuth2UserInfo.getName().substring(oAuth2UserInfo.getName().indexOf(" ")+1));
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setRole(Constant.ROLE_USER);
        user.setPassword( user.getProvider());
        user.setEnable(true);
        return usersRepo.save(user);
    }


}
