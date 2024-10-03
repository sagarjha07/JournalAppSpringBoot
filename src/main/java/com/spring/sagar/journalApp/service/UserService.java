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
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepository.save(user);
    }

    public void updateUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDB = userRepository.findByUsername(username);
        if(userInDB != null) {
            userInDB.setPassword(user.getPassword());
            userInDB.setUsername(user.getUsername());
            saveNewUser(userInDB);
        }
    }

}
