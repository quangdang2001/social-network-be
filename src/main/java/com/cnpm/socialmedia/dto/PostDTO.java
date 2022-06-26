package com.cnpm.socialmedia.dto;

import com.cnpm.socialmedia.model.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String content;
    private String urlImage;
    private Long userId;
    private int countLiked = 0;
    private int countCmted = 0;
    private int countShated = 0;
    private int countReported = 0;
    private Date createTime;
    private Date updateTime;
    private boolean isPostShare;

    private Long postSharedId;
    private PostShareDTO postShared;

    public PostDTO(Long id, String content, String urlImage, Long userId, int countLiked, int countCmted, int countShated, int countReported, Date createTime, Date updateTime, boolean isPostShare) {
        this.id = id;
        this.content = content;
        this.urlImage = urlImage;
        this.userId = userId;
        this.countLiked = countLiked;
        this.countCmted = countCmted;
        this.countShated = countShated;
        this.countReported = countReported;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isPostShare = isPostShare;
    }

    private UserDTO userCreate;
    private boolean isLiked;
}
