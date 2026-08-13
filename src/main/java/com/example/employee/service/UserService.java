package com.example.employee.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.employee.dto.RegisterRequest;
import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

   private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
    throw new RuntimeException("Username already exists");
}
       
    User user =new User();

    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(request.getRole());

    userRepository.save(user);

    return "User registered successfully";
    }

}
