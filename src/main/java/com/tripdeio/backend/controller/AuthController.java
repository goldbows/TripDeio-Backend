package com.tripdeio.backend.controller;

import com.tripdeio.backend.dto.AppUserDTO;
import com.tripdeio.backend.dto.LoginRequest;
import com.tripdeio.backend.dto.LoginResponse;
import com.tripdeio.backend.dto.SignupRequest;
import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.security.JwtTokenProvider;
import com.tripdeio.backend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            String token = jwtTokenProvider.generateToken(loginRequest.getEmail(), isAdmin);

            AppUser user = appUserService.findByEmail(loginRequest.getEmail());
            AppUserDTO userDto = new AppUserDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());

            return ResponseEntity.ok(new LoginResponse(token, isAdmin, null, userDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(null, false, "Invalid credentials"));
        }
    }
}