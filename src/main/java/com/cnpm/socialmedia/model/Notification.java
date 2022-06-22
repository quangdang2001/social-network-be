package com.cnpm.socialmedia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    @CreationTimestamp
    private Date createTime;
    @Convert(attributeName = "booleanToInteger")
    private boolean isSeen = false;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Users userReceiver;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Users userCreate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
