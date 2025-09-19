package org.academo.academo.service;

import org.academo.academo.dto.LoginForm;
import org.academo.academo.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void createUser(UserDTO user);

    void removeUser(UUID userId);

    List<UserDTO> getAllUsers();

    String login(LoginForm loginForm);

    UUID getIdByUsername(String username);

    UserDTO getByUserId(UUID userId);

    String getUsernameById(UUID userId);
}
