package com.example.taskmanager.service;

import com.example.taskmanager.model.User;

import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> login(String username);
}
