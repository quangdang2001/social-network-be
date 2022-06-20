package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.PasswordDTO;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.event.RegisterCompleteEvent;
import com.cnpm.socialmedia.model.ModelRegister.VerificationToken;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.service.email.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request){
        Users users = userService.saveRegister(userDTO);
        if (users==null){
            return ResponseEntity.ok(new ResponseDTO(false,"Email already exits",
                    null));
        }
        publisher.publishEvent(new RegisterCompleteEvent(
                users,
                applicationUrl(request)
        ));

        return ResponseEntity.ok(new ResponseDTO(true,"Sent email",
                null));
    }

    @RequestMapping(value = "/verifyRegistration", method = RequestMethod.GET)
    public ResponseEntity<?> verifyRegistration(@RequestParam("email") String email, @RequestParam("token") String token) {
        String result = userService.validateVerificationToken(email,token);
        if(result.equalsIgnoreCase("valid")) {
            return ResponseEntity.ok(new ResponseDTO(true,"Success",
                    null));
        }
        return ResponseEntity.ok(new ResponseDTO(false,"Bad user",
                null));
    }

    @GetMapping("/resendVerifyToken")
    public ResponseEntity<?> resendVerificationToken(@RequestParam("email") String email,
                                          HttpServletRequest request) throws MessagingException {
        VerificationToken verificationToken
                = userService.SendToken(email);
        Users user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return ResponseEntity.ok(new ResponseDTO(true,"Verification Link Sent",
                null));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request) throws MessagingException {
        Users user = userService.findUserByEmail(passwordDTO.getEmail());
        if (user!= null && user.isEnable()){
            String token = "";
            token = userService.SendToken(passwordDTO.getEmail()).getToken();

            //Send email
            emailSenderService.sendEmail(user.getEmail(),token,"Reset Password Token");
            log.info("Reset password: {}",
                    token);
            return ResponseEntity.ok(new ResponseDTO(true,"Sent email reset token",
                    null));
        }
        return ResponseEntity.badRequest().body(new ResponseDTO(false,"Not found email",
                null));
    }
    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordDTO passwordDTO) {


        String result = userService.validatePasswordResetToken(passwordDTO.getEmail(), token);
        if(!result.equalsIgnoreCase("valid")) {

            return ResponseEntity.badRequest().body(new ResponseDTO(false,"Invalid token",
                    null));
        }
        Optional<Users> user = Optional.ofNullable(userService.findUserByEmail(passwordDTO.getEmail()));
        if(user.isPresent()) {
            if (!user.get().isEnable()){
                return ResponseEntity.ok().body(new ResponseDTO(false,"Email not verify",
                        null));
            }
            userService.changePassword(user.get(), passwordDTO.getNewPassword());
            return ResponseEntity.ok().body(new ResponseDTO(true,"Change password successfully",
                    null));
        } else {
            return ResponseEntity.ok().body(new ResponseDTO(false,"email not found",
                    null));
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDTO passwordDTO){
        Users user = userService.findUserByEmail(passwordDTO.getEmail());
        if(!userService.checkIfValidOldPassword(user,passwordDTO.getOldPassword())) {
            return ResponseEntity.ok().body(new ResponseDTO(false,"Invalid Old Password",
                    null));
        }
        //Save New Password
        userService.changePassword(user,passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new ResponseDTO(true,"Password Changed Successfully",
                null));
    }


    private void resendVerificationTokenMail(Users user, String applicationUrl, VerificationToken verificationToken) throws MessagingException {
        String url =
                applicationUrl
                        + "/api/verifyRegistration?token="
                        + verificationToken.getToken()
                        + "&email="
                        + user.getEmail();

        //sendVerificationEmail()
        String format = String.format("Click the link to verify your account: %s",url);
        emailSenderService.sendEmail(user.getEmail(),format, "Verify Registration");
        log.info("Click the link to verify your account: {}",
                url);
    }
    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
