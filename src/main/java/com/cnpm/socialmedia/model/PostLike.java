package com.cnpm.socialmedia.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(PostLikeID.class)
public class PostLike {
    @Id
    @ManyToOne
    private Post postId;
    @Id
    @ManyToOne
    private Users userId;
}
