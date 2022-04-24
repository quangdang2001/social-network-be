package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.PasswordDTO;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.event.RegisterCompleteEvent;
import com.cnpm.socialmedia.model.ModelRegister.VerificationToken;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request){
        Users users = userService.save(userDTO);
        publisher.publishEvent(new RegisterCompleteEvent(
                users,
                applicationUrl(request)
        ));

        return ResponseEntity.ok("Sent email");

    }

    @RequestMapping(value = "/verifyRegistration", method = RequestMethod.GET)
    public String verifyRegistration(@RequestParam("email") String email, @RequestParam("token") String token) {
        String result = userService.validateVerificationToken(email,token);
        if(result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully";
        }
        return "Bad User";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("email") String email,
                                          HttpServletRequest request) {
        VerificationToken verificationToken
                = userService.SendToken(email);
        Users user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return "Verification Link Sent";
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request) {
        Users user = userService.findUserByEmail(passwordDTO.getEmail());
        if (user!= null && user.isEnable()){
            String token = "";
            token = userService.SendToken(passwordDTO.getEmail()).getToken();

            //Send email
            log.info("Reset password: {}",
                    token);
            return ResponseEntity.ok("Sent email reset token");
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found email");

    }
    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordDTO passwordDTO) {


        String result = userService.validatePasswordResetToken(passwordDTO.getEmail(), token);
        if(!result.equalsIgnoreCase("valid")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
        Optional<Users> user = Optional.ofNullable(userService.findUserByEmail(passwordDTO.getEmail()));
        if(user.isPresent()) {
            if (!user.get().isEnable()){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email not verify");
            }
            userService.changePassword(user.get(), passwordDTO.getNewPassword());
            return ResponseEntity.ok("Change password successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email not found");

        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordDTO passwordDTO){
        Users user = userService.findUserByEmail(passwordDTO.getEmail());
        if(!userService.checkIfValidOldPassword(user,passwordDTO.getOldPassword())) {
            return "Invalid Old Password";
        }
        //Save New Password
        userService.changePassword(user,passwordDTO.getNewPassword());
        return "Password Changed Successfully";
    }
    private void resendVerificationTokenMail(Users user, String applicationUrl, VerificationToken verificationToken) {
        String url =
                applicationUrl
                        + "/verifyRegistration?token="
                        + verificationToken.getToken();

        //sendVerificationEmail()
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
