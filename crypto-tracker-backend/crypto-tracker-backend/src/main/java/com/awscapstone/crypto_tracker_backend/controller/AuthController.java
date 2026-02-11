package com.awscapstone.crypto_tracker_backend.controller;

import com.awscapstone.crypto_tracker_backend.dto.LoginRequest;
import com.awscapstone.crypto_tracker_backend.dto.RegisterRequest;
import com.awscapstone.crypto_tracker_backend.model.User;
import com.awscapstone.crypto_tracker_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        try {
            String username = request.getUsername();
            String email = request.getEmail();
            String password = request.getPassword();

            log.info("Registering new user: {}", username);

            User user = userService.registerUser(username, email, password);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Registered successfully");
            response.put("user", Map.of(
                    "username", user.getUsername(),
                    "email", user.getEmail()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            String username = request.getUsername();
            String password = request.getPassword();

            log.info("Login attempt for user: {}", username);

            User user = userService.authenticateUser(username, password);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logged in successfully");
            response.put("user", Map.of(
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "role", user.getRole()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid username or password");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/check/{username}")
    public ResponseEntity<Map<String, Object>> checkUsername(@PathVariable String username) {
        boolean exists = userService.userExists(username);

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("username", username);

        return ResponseEntity.ok(response);
    }
}