package com.spring.sagar.journalApp.service;

import com.spring.sagar.journalApp.entity.User;
import com.spring.sagar.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordencoder = new BCryptPasswordEncoder();

    public void saveNewUser(User user) {
        try{
            user.setPassword(passwordencoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving new user", e);
        }
    }

    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving user", e);
        }
    }

    public void updateUser(User user) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User userInDB = userRepository.findByUsername(username);
            if(userInDB != null) {
                userInDB.setPassword(user.getPassword());
                userInDB.setUsername(user.getUsername());
                saveNewUser(userInDB);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating user", e);
        }
    }

    public List<User> getAllUsers() {
        try{
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error while getting all users", e);
        }
    }

}
