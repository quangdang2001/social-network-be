package com.cnpm.socialmedia.dto;


import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
public class CmtResponse {
    private Long cmtId;
    private String content;
    private Date createTime;
    private User userCmt;
    private Long cmtParrentId =null;

    @AllArgsConstructor
    @Data
    public static class User {
        private Long id;
        private String firstName;
        private String lastName;
        private String urlImage;
    }
}



