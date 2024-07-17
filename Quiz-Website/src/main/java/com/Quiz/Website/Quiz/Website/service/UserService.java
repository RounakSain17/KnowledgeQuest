package com.Quiz.Website.Quiz.Website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Quiz.Website.Quiz.Website.model.User;
import com.Quiz.Website.Quiz.Website.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(User user)
    {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        return userRepository.save(user);
    }

    public User login(String username,String password)
    {
        User user = userRepository.findByUsername(username);
        if(user!=null && user.getPassword().equals(password))
        {
            return user;
        }

        return null;
    }
}
