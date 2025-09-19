package org.academo.academo.model;

import java.util.UUID;

public class User {
    private UUID id;
    private String fullName;
    private String username;
    private String password;
    private String role;

    public User(){}

    public User(UUID id, String fullName, String username, String password, String role) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, UUID id, String fullName, String role) {
        this.username = username;
        this.password = password;
        
        this.fullName = fullName;

    }

    public User(String username, String password, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }
}
