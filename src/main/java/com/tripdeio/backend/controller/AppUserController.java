package com.tripdeio.backend.controller;

import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app-user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

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