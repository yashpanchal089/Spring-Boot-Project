package com.yash.journalApp.controller;

import com.yash.journalApp.entity.User;
import com.yash.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Ok";
    }

    // Create new user
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user){
        userService.saveNewUser(user); // password will be encoded
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
