package com.cnpm.socialmedia.config.OAuth2;

import com.cnpm.socialmedia.exception.AppException;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.repo.UserRepo;
import com.cnpm.socialmedia.service.oauth2.OAuth2UserIplm;
import com.cnpm.socialmedia.utils.Constant;
import com.cnpm.socialmedia.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepo userRepo;
    @Value("${redirect.url.google}")
    private String redirectUrl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserIplm oAuth2UserIplm = (OAuth2UserIplm) authentication.getPrincipal();

        String email = oAuth2UserIplm.getAttributes().get("email").toString();

        Users users = userRepo.findUserByEmail(email);

        if (users==null){
            users = new Users();
            users.setEmail(email);
            users.setLastName(oAuth2UserIplm.getAttributes().get("family_name").toString());
            users.setFirstName(oAuth2UserIplm.getAttributes().get("given_name").toString());
            users.setImageUrl(oAuth2UserIplm.getAttributes().get("picture").toString());
            users.setEnable((Boolean) oAuth2UserIplm.getAttributes().get("email_verified"));
            users.setPassword("google");
            users.setOauth2(true);
            users.setRole(Constant.ROLE_USER);
            userRepo.save(users);
        }

        String token = Utils.jwtProvide(users,request);

        String targetUrl = determineTargetUrl(users,request,response,authentication);
        response.setHeader("token",token);

        getRedirectStrategy().sendRedirect(request, response,targetUrl );

    }
    protected String determineTargetUrl(Users users,HttpServletRequest request, HttpServletResponse response, Authentication authentication) {


        String token = Utils.jwtProvide(users,request);

        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

}
