package com.tripdeio.backend.utils;

import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.service.AppUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private final AppUserService appUserService;

    public SecurityUtil(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    public AppUser getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return appUserService.findByEmail(userDetails.getUsername());
        }
        return null;
    }
}
