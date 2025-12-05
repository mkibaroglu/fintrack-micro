package com.fintrack.gateway_service.dto;

public class TokenValidationResponse {
    private boolean valid;
    private String username;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
