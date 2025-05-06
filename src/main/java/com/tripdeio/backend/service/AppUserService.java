package com.tripdeio.backend.service;

import com.tripdeio.backend.dto.SignupRequest;
import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void signup(SignupRequest signupRequest) throws Exception {
        if (appUserRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        AppUser user = new AppUser();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());
        user.setPasswordHash(hashedPassword);
        user.setAdmin(false);
        appUserRepository.save(user);
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

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}