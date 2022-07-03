package com.cnpm.socialmedia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    @CreationTimestamp
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
    private Comment commentParrent;

    @JsonIgnore
    @OneToMany(mappedBy = "commentParrent",cascade = CascadeType.REMOVE)
    private List<Comment> comments;

//    public void increaseLike(){
//        this.countLike++;
//    }


}
