package com.cnpm.socialmedia.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    public static Long getIdCurrentUser(){
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
}
