package com.cnpm.socialmedia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ImagePost> images;

    private int countLiked = 0;
    private int countCmted = 0;
    private int countShated = 0;
    private int countReported = 0;
    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @JsonIgnore
    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<Comment> comments;

    private boolean isPostShare=false;
    @ManyToOne(fetch = FetchType.EAGER)
    private Post postShared = null;


    public void increaseLike(){
        this.countLiked++;
    }
    public void decreaseLike(){
        this.countLiked--;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "postShared")
    private List<Post> postChild;

    @JsonIgnore
    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    private List<Notification> notifications;

    @JsonIgnore
    @OneToMany(mappedBy = "postId",cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes;

    @PreRemove
    public void setNull(){
        postChild.forEach(post -> {
            post.setPostShared(null);
        });
    }
}
