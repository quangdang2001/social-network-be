package com.cnpm.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmtDTO {
    private Long id;
    private String content;

    private Long userId;

    private Long postId;
    private Long cmtId;
}
