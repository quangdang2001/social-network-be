package com.cnpm.socialmedia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private Date createTime;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users users;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private boolean isCommentPost = true;
    private Integer countReply = 0;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

//    public void increaseLike(){
//        this.countLike++;
//    }

}
