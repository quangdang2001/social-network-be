package com.cnpm.socialmedia.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,
                          String body,
                          String subject) throws MessagingException {
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage);

        mimeMailMessage.setContent(body,"text/html");

        helper.setFrom("socialnetwork99.me@gmail.com");
        helper.setTo(toEmail);
        helper.setText(body,true);
        helper.setSubject(subject);

        mailSender.send(mimeMailMessage);
        System.out.println("Mail send...");
    }
}
