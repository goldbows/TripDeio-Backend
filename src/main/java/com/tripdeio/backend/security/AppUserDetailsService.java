package com.tripdeio.backend.security;

import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public AppUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> user = appUserRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new AppUserDetails(user.orElseThrow());
    }
}
