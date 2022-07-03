package com.cnpm.socialmedia.event;

import com.cnpm.socialmedia.model.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegisterCompleteEvent extends ApplicationEvent {
    private Users user;
    private String url;

    public RegisterCompleteEvent(Users user, String url) {
        super(user);
        this.user = user;
        this.url = url;
    }
}
