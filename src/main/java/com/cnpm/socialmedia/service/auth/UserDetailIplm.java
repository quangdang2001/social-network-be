package com.cnpm.socialmedia.service.auth;

import com.cnpm.socialmedia.model.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
public class UserDetailIplm implements UserDetails, OAuth2User {

    private Users users;
    private Map<String, Object> attributes;

    public UserDetailIplm(Users users) {
        this.users = users;
    }
    public static UserDetailIplm create(Users users, Map<String,Object> attributes){
        UserDetailIplm userDetailIplm = new UserDetailIplm(users);
        userDetailIplm.setAttributes(attributes);
        return userDetailIplm;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public String getName() {
        return users.getId().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(users.getRole()));
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return users.isEnable();
    }

}
