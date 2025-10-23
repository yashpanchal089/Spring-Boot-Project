package com.yash.journalApp.controller;

import com.yash.journalApp.entity.User;
import com.yash.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Update username and/or password
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String currentUsername = authentication.getName();
        User userInDb = userService.findByUserName(currentUsername);
        if (userInDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Update username if provided
        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            userInDb.setUserName(user.getUserName());
        }

        // Update password if provided and encode it
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userInDb.setPassword(userService.encodePassword(user.getPassword()));
        }

        userService.saveEntry(userInDb);

        // Return the updated username to the client
        return ResponseEntity.ok("Username updated to: " + userInDb.getUserName());
    }
}
