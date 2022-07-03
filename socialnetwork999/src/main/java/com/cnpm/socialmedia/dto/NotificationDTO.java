package com.cnpm.socialmedia.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String content;

    private Date createTime;
    private boolean isSeen;

    private Long postId;
    private Long userReceiver;

    private UserDTO userCreate;

//    private NotificationDTO notificationDTO;

}
