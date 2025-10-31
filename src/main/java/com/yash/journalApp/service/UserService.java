package com.yash.journalApp.service;

import com.yash.journalApp.entity.User;
import com.yash.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveEntry(User user){
        userRepository.save(user);
    }
    public boolean saveNewUser(User user) {
        try {
            // ✅ Check if username already exists
            if (userRepository.findByUserName(user.getUserName()) != null) {
                logger.info("User with username '{}' already exists", user.getUserName());
                return false;
            }
            // ✅ Encrypt password and assign role
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            logger.info("User saved successfully");
            return true;
        } catch (Exception e) {
            logger.error("Error Occured for {}:  ",user.getUserName(), e);
//            logger.warn("hahaha");
//            logger.info("hahaha");
//            logger.debug("hahaha");
//            logger.trace("hahaha");

            return false;
        }
    }

    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id){
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
