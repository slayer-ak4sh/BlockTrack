package com.awscapstone.crypto_tracker_backend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String index(){
        return "Crypto Tracker Backend is running!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
