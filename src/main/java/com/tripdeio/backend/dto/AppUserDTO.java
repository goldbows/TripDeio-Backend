package com.tripdeio.backend.dto;

public class AppUserDTO {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private boolean isAdmin;

    public AppUserDTO(Long id, String username, String email, boolean admin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isAdmin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
