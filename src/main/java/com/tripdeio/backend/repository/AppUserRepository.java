package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    Optional<AppUser> findByEmail(String email);
}
