package com.tripdeio.backend.repository;

import com.tripdeio.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
}
