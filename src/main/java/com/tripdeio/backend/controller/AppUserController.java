package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.LoginRequest;
import com.tripdeio.backend.dto.LoginResponse;
import com.tripdeio.backend.dto.SignupRequest;
import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        try {
            appUserService.signup(signupRequest);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = appUserService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(null, false, e.getMessage()));
        }
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = appUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/admin/users/{userId}/toggle-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> toggleAdminStatus(@PathVariable Long userId, @RequestBody Map<String, Boolean> body) {
        try {
            boolean isAdmin = body.get("isAdmin");
            appUserService.toggleAdminStatus(userId, isAdmin);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}