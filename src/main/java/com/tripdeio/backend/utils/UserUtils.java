package com.tripdeio.backend.utils;

import com.tripdeio.backend.entity.AppUser;
import com.tripdeio.backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * Get the current logged-in user's ID.
     * Assumes JWT authentication and that the username is stored in the security context.
     *
     * @return Current user's ID
     */
//    public Long getCurrentUserId() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        AppUser appUser = appUserRepository.findByUsername(username);
//        return appUser != null ? appUser.getId() : null;
//    }
//
//    /**
//     * Get the current logged-in user.
//     *
//     * @return Current user
//     */
//    public AppUser getCurrentUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return appUserRepository.findByUsername(username);
//    }
}
