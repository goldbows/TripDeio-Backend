package com.tripdeio.backend.dto;

public class LoginResponse {
    private String token;
    private boolean isAdmin;
    private AppUserDTO appUserDTO;
    private String message;

    public LoginResponse(String token, boolean isAdmin) {
        this.token = token;
        this.isAdmin = isAdmin;
    }

    public LoginResponse(String token, boolean isAdmin, String message) {
        this.token = token;
        this.isAdmin = isAdmin;
        this.message = null;
    }

    public LoginResponse(String token, boolean isAdmin, String message, AppUserDTO appUserDTO) {
        this.token = token;
        this.isAdmin = isAdmin;
        this.message = message;
        this.appUserDTO = appUserDTO;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AppUserDTO getAppUserDTO() {
        return appUserDTO;
    }

    public void setAppUserDTO(AppUserDTO appUserDTO) {
        this.appUserDTO = appUserDTO;
    }
}