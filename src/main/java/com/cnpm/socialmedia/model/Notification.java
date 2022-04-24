package com.cnpm.socialmedia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private String urlPost;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;
}
