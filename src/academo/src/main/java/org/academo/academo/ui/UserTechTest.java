package org.academo.academo.ui;

import org.academo.academo.Exception.AlreadyExistsException;
import org.academo.academo.Exception.DatabaseServiceException;
import org.academo.academo.Exception.InvalidDataException;
import org.academo.academo.Exception.ResourceNotFoundException;
import org.academo.academo.dto.UserDTO;
import org.academo.academo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Profile("cli")
public class UserTechTest {

    @Autowired private UserServiceImpl userService;
    @Autowired private InputProvider inputProvider;

    // Handle User Creation
    public void handleCreateUser(){
        UserDTO user = inputProvider.inputUserDTO(); // Get input from user (you already have this method)

        try {
            userService.createUser(user);
            System.out.println("✅ User created successfully: " + user.getUsername());
        } catch (AlreadyExistsException e) {
            System.out.println("❌ That username already exists. Please try a different one.");
        } catch (InvalidDataException e) {
            System.out.println("❌ Invalid user data. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❗ Unexpected error: " + e.getMessage());
        }
    }

    // Handle User Removal
    public void handleRemoveUser() {
        UUID userId = inputProvider.promptUUID("Enter user UUID to remove");

        try {
            userService.removeUser(userId);
            System.out.println("✅ User removed successfully with ID: " + userId);
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ User not found with ID: " + userId);
        } catch (Exception e) {
            System.out.println("❗ Unexpected error: " + e.getMessage());
        }
    }

    // Handle Fetching All Users
    public void handleGetAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("⚠️ No users found.");
            } else {
                System.out.println("✅ List of all users:");
                for (UserDTO user : users) {
                    System.out.println(user.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("❗ Error fetching users: " + e.getMessage());
        }
    }

    // Handle Fetching User by ID
    public void handleGetUserById() {
        UUID userId = inputProvider.promptUUID("Enter user UUID to fetch");

        try {
            UserDTO user = userService.getByUserId(userId);
            System.out.println("✅ User found: " + user.getUsername());
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ User not found with ID: " + userId);
        } catch (Exception e) {
            System.out.println("❗ Unexpected error: " + e.getMessage());
        }
    }
}
