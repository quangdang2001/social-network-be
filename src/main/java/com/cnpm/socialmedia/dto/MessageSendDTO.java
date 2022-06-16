package com.cnpm.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class MessageSendDTO {
    private String content;
    private Long userSenderId;
    private Long userReceiverId;
}
