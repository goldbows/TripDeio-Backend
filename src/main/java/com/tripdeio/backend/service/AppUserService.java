package com.tripdeio.backend.service;

import com.tripdeio.backend.dto.LoginRequest;
import com.tripdeio.backend.dto.LoginResponse;
import com.tripdeio.backend.dto.SignupRequest;
import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.repository.AppUserRepository;
import com.tripdeio.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;
import javax.crypto.spec.IvParameterSpec;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private static final String ENCRYPTION_KEY = "aB3dF5hJ7kM9nP2rT4vW6yZ8cE1gI4kL";
    private static final String INIT_VECTOR = "1234567890123456";

    private String decryptPassword(String encryptedPassword) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(ENCRYPTION_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(original);
    }

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        AppUser user = appUserRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new RuntimeException("Email or Password is incorrect");
        }

        if (!user.getPasswordHash().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Email or Password is incorrect");
        }

        String token = jwtService.generateToken(user.getEmail(), user.isAdmin());
        return new LoginResponse(token, user.isAdmin());
    }

    public AppUser signup(SignupRequest signupRequest) throws Exception {
        if (appUserRepository.findByEmail(signupRequest.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        AppUser user = new AppUser();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        String decryptedPassword = decryptPassword(signupRequest.getPassword());
        user.setPasswordHash(passwordEncoder.encode(decryptedPassword));
        user.setAdmin(false);
        return appUserRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public void toggleAdminStatus(Long userId, boolean isAdmin) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAdmin(isAdmin);
        appUserRepository.save(user);
    }
}