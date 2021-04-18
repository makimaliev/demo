package com.example.demo.controller;

import com.example.demo.payload.*;
import com.example.demo.security.CurrentUser;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class MainController {

    private final MainService service;

    public MainController(MainService service){
        this.service = service;
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = this.service.authenticate(loginRequest);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        this.service.logoutUser(request);
        return ResponseEntity.ok("User logged out successfully.");
    }

    @GetMapping("/api/users/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable(value = "username") String username) {
        UserProfile profile = this.service.getUser(username);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/api/payment")
    public ResponseEntity<?> makePayment(@CurrentUser UserPrincipal currentUser) {
        UserProfile profile = this.service.makePayment(currentUser.getUsername());
        return ResponseEntity.ok(profile);
    }
}
