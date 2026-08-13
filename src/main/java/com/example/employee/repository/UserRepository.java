package com.example.employee.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee.model.User;

public interface  UserRepository extends JpaRepository<User, Integer> {

     Optional<User> findByUsername(String username);
     Optional<User> findByProviderAndProviderId(
        String provider,
        String providerId);
}
