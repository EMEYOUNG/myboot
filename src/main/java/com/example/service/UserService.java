package com.example.service;

import com.example.entity.User;
import org.springframework.stereotype.Service;



public interface UserService {
    public User userLogin(String username, String password);
}
