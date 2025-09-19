// src/test/java/org/academo/academo/builders/UserBuilder.java
package org.academo.academo.builders;

import org.academo.academo.model.User;
import org.academo.academo.dto.UserDTO; // <-- If you don't have this, remove asDto() + this import.

import java.util.UUID;

public class UserBuilder {
    private UUID id = UUID.fromString("00000000-0000-0000-0000-00000000A001");
    private String fullName = "User Test";
    private String username = "Rockio";
    private String password = "student";
    private String role = "USER";

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public UserBuilder withoutId() {
        this.id = null;
        return this;
    }

    public UserBuilder withFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    /**
     * Build the domain entity (matches your constructor new User(username, password, fullName, role))
     */
    public User asEntity() {
        User u = new User(username, password, fullName, role);
        u.setId(id); // id may be null (e.g., for insert tests)
        return u;
    }

    /**
     * Build the DTO. If you don't have UserDTO in your project, delete this and its import.
     */
    public UserDTO asDto() {
        return new UserDTO(username, password, id, fullName, role);
    }

    //public UserDTO(String username, String password, UUID id, String fullName, String role) {
}
