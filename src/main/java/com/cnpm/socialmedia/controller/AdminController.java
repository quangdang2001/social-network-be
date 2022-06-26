package com.cnpm.socialmedia.controller;

import com.cnpm.socialmedia.dto.ResponseDTO;
import com.cnpm.socialmedia.model.Users;
import com.cnpm.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam int page,
                                     @RequestParam int size){
        Page<Users> usersList = userService.getUserAdmin(page,size);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",usersList));
    }

    
}
