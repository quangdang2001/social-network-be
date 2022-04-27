package com.cnpm.socialmedia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserFollowingID.class)
public class UserFollowing implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Users userId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Users followingId;

}
