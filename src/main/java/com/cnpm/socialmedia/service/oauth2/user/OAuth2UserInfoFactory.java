package com.cnpm.socialmedia.service.oauth2.user;

import com.cnpm.socialmedia.exception.OAuth2AuthenticationProcessingException;
import com.cnpm.socialmedia.utils.Constant;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(Constant.GOOGLE)) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
