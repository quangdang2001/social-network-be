package com.cnpm.socialmedia.event.listener;

import com.cnpm.socialmedia.event.RegisterCompleteEvent;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegisterCompleteEventListener implements ApplicationListener<RegisterCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegisterCompleteEvent event) {
        Users user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);

        // Send mail to user
        String url =
                event.getUrl()
                        + "/api/verifyRegistration?email="
                        + user.getEmail()
                        + "&token="+
                        token;
        // SendVerificationEmail
        System.out.println(url);
    }
}
