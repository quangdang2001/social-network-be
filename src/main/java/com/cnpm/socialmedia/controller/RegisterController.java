package com.cnpm.socialmedia.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cnpm.socialmedia.dto.LoginRequest;
import com.cnpm.socialmedia.dto.PasswordDTO;
import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.dto.UserDTO;
import com.cnpm.socialmedia.event.RegisterCompleteEvent;
import com.cnpm.socialmedia.exception.AppException;
import com.cnpm.socialmedia.model.ModelRegister.VerificationToken;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.UserService;
import com.cnpm.socialmedia.service.auth.UserDetailIplm;
import com.cnpm.socialmedia.service.email.EmailSenderService;
import com.cnpm.socialmedia.utils.EmailTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "AUTHORIZATION")
public class RegisterController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final EmailSenderService emailSenderService;
    private final AuthenticationManager authenticationManager;

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
    public RedirectView verifyRegistration(@RequestParam("email") String email, @RequestParam("token") String token) {
        String result = userService.validateVerificationToken(email,token);
        if(result.equalsIgnoreCase("valid")) {
            return new RedirectView("https://www.qpnetwork.tk/confirmemailqpnetwork");
        }
        return new RedirectView("https://www.qpnetwork.tk/confirmemailfailed");
    }

    @GetMapping("/resendVerifyToken")
    public ResponseEntity<?> resendVerificationToken(@RequestParam("email") String email,
                                          HttpServletRequest request) throws MessagingException {
        VerificationToken verificationToken
                = userService.SendToken(email);

        resendVerificationTokenMail(email, applicationUrl(request), verificationToken);
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
            emailSenderService.sendEmail(user.getEmail(), EmailTemplate.emailToken(token),"Reset Password Token");
            log.info("Reset password: {}",
                    token);
            return ResponseEntity.ok(new ResponseDTO(true,"Sent email reset token",
                    null));
        }
        return ResponseEntity.badRequest().body(new ResponseDTO(false,"Not found email",
                null));
    }
    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@RequestBody PasswordDTO passwordDTO) {
        Users result = userService.validatePasswordResetToken(passwordDTO.getToken());
        if(result == null) {

            return ResponseEntity.badRequest().body(new ResponseDTO(false,"Invalid token",
                    null));
        }
        if (!result.isEnable()){
            return ResponseEntity.ok().body(new ResponseDTO(false,"Email not verify",
                    null));
        }
        userService.changePassword(result, passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new ResponseDTO(true,"Change password successfully",
                null));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDTO passwordDTO){
        Users user = userService.findUserByEmail(passwordDTO.getEmail());
        if(!userService.checkIfValidOldPassword(user,passwordDTO.getOldPassword())) {
            return ResponseEntity.ok().body(new ResponseDTO(false,"Invalid Old Password",
                    null));
        }
        //Save New Password
        userService.changePassword(user, passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new ResponseDTO(true,"Password Changed Successfully",
                null));
    }
    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){

            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Users users = userService.findById(Long.parseLong(username));
                String access_token = JWT.create()
                        .withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000*6*24*15))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("role",users.getRole())
                        .sign(algorithm);

                refresh_token = JWT.create()
                        .withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis()*2))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);

                Map<String,String> token = new HashMap<>();
                token.put("access_token",access_token);
                token.put("refresh_token",refresh_token);
                token.put("userId",username);
                token.put("role", users.getRole());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), token);
            }catch (Exception e){
                System.out.println("Error loggin in: "+e.getMessage());
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                    HttpServletRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailIplm user = (UserDetailIplm) authentication.getPrincipal();

            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

            String access_token = JWT.create()
                    .withSubject(user.getUsers().getId().toString())
                    .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000*6*24*15))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("role",user.getUsers().getRole())
                    .sign(algorithm);

            Map<String,String> token = new HashMap<>();
            token.put("access_token",access_token);
            token.put("userId",user.getUsers().getId().toString());
            token.put("role", user.getUsers().getRole());
            return ResponseEntity.ok(token);
        }catch (Exception e){
            throw new AppException(403,"Access Denied");
        }
    }

    private void resendVerificationTokenMail(String email, String applicationUrl, VerificationToken verificationToken) throws MessagingException {
        String url =
                applicationUrl
                        + "/api/verifyRegistration?token="
                        + verificationToken.getToken()
                        + "&email="
                        + email;

        //sendVerificationEmail()
        emailSenderService.sendEmail(email,EmailTemplate.emailRegister(url), "Verify Registration");
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
