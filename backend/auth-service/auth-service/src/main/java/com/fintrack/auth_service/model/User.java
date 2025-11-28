package com.fintrack.auth_service.model;

public class User {
    private String username;
    private String password; // demo için plain text, sonra encode ederiz
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
