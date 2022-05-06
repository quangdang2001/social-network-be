package com.cnpm.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private String message;
    private Date createTime;

    private Sender sender;
    private Receiver receiver;

    @Data
    @AllArgsConstructor
    public static class Sender{
        private Long id;
        private String firstName;
        private String lastName;
        private String urlImage;
    }
    @Data
    @AllArgsConstructor
    public static class Receiver {
        private Long id;
        private String firstName;
        private String lastName;
        private String urlImage;
    }
}
