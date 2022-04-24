package com.cnpm.socialmedia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data @AllArgsConstructor
@NoArgsConstructor
public class Users {

    public enum Sex{
        MALE,FEMALE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(nullable = false,unique = true)
    private String email;
    @NotNull
    private String password;
    private int gender;
    private String bio;
    private String address;
    private int countReport = 0;
    private boolean enable = false;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Users> followers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Users> followings;

    @JsonIgnore
    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @JsonIgnore
    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comment;
}
