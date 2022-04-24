package com.cnpm.socialmedia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostShared {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private int countLiked = 0;
    private int countCommented = 0;
    private Date createTime;
    @OneToMany(mappedBy = "postShared",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;
}
