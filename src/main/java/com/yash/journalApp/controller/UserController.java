package com.yash.journalApp.controller;

import com.yash.journalApp.api.response.WeatherResponse;
import com.yash.journalApp.entity.User;
import com.yash.journalApp.repository.UserRepository;
import com.yash.journalApp.service.UserService;
import com.yash.journalApp.service.WeatherService;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

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

        // Update email if provided
        if(user.getEmail() != null && !user.getEmail().isEmpty()){
            userInDb.setEmail(user.getEmail());
        }

        userService.saveEntry(userInDb);

        // Return the updated username to the client
        return ResponseEntity.ok("Username updated to: " + userInDb.getUserName());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String greeting = "";
        if(weatherResponse != null){
            greeting = ", Weather feels like " + weatherResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hello " + authentication.getName()+ greeting, HttpStatus.OK);
    }


}
