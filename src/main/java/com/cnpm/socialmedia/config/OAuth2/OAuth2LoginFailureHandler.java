package com.cnpm.socialmedia.config.OAuth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${redirect.url.google}")
    private String redirectUrl;
    public OAuth2LoginFailureHandler() {

    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String tagetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("error", exception.getMessage())
                .build().toUriString();

    getRedirectStrategy().sendRedirect(request,response,tagetUrl);
    }
}
