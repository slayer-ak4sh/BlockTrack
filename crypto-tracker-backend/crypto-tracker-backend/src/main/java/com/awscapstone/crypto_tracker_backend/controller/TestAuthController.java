package com.awscapstone.crypto_tracker_backend.controller;

import com.awscapstone.crypto_tracker_backend.dto.LoginRequest;
import com.awscapstone.crypto_tracker_backend.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/test/auth")
@Slf4j
public class TestAuthController {

    private static final Map<String, Map<String, String>> testUsers = new ConcurrentHashMap<>();

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterRequest request) {
        log.info("Test registration for user: {}", request.getUsername());
        
        if (testUsers.containsKey(request.getUsername())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "User already exists");
            return response;
        }

        Map<String, String> userData = new HashMap<>();
        userData.put("username", request.getUsername());
        userData.put("email", request.getEmail());
        userData.put("password", request.getPassword());
        testUsers.put(request.getUsername(), userData);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Registered successfully");
        response.put("data", Map.of(
            "user", Map.of(
                "username", request.getUsername(),
                "email", request.getEmail()
            )
        ));
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        log.info("Test login for user: {}", request.getUsername());
        
        Map<String, String> userData = testUsers.get(request.getUsername());
        
        if (userData == null || !userData.get("password").equals(request.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return response;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged in successfully");
        response.put("data", Map.of(
            "user", Map.of(
                "username", userData.get("username"),
                "email", userData.get("email"),
                "role", "USER"
            )
        ));
        return response;
    }
}
