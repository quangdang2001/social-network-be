package com.cnpm.socialmedia.dto;

import com.cnpm.socialmedia.model.Post;
import lombok.Data;

@Data
public class PostShareDTO {
    private Post postShared;
    private Long userCreateId;
    private String firstName;
    private String lastName;
    private String avatar;
}
