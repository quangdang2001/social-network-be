package com.cnpm.socialmedia.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    private String email;
    private String token;
    private String oldPassword;
    private String newPassword;
}
