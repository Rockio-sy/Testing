package org.academo.academo.service.impl;

import org.academo.academo.Exception.*;
import org.academo.academo.dto.LoginForm;
import org.academo.academo.dto.UserDTO;
import org.academo.academo.model.User;
import org.academo.academo.repository.impl.UserRepositoryImpl;
import org.academo.academo.service.UserService;
import org.academo.academo.util.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private Converter converter;

    @Override
    public void createUser(UserDTO user) {
        User model = converter.DTOtoUser(user);
        try {
            userRepositoryImpl.saveUser(model);
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistsException("Username already exists ", e);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Missing or invalid data fields ", e);
        } catch (DataAccessException e) {
            throw new DatabaseServiceException("Failed to save user ", e);
        }
    }

    @Override
    public void removeUser(UUID userId) {
        try {
            userRepositoryImpl.removeUser(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("User not found with ID " + userId, e);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepositoryImpl.getAll();
        List<UserDTO> dto = new ArrayList<>();
        for (User u : users) {
            UserDTO userDTO = converter.userToDTO(u);
            dto.add(userDTO);
        }
        return dto;
    }


    @Override
    public UserDTO getByUserId(UUID userId) {
        try {
            Optional<User> model = userRepositoryImpl.getByUserId(userId);
            if (model.isEmpty()) {
                throw new ResourceNotFoundException("User not found with ID " + userId);
            }
            return converter.userToDTO(model.get());
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("User not found with ID " + userId, e);
        }
    }

    @Override
    public String login(LoginForm loginForm) {
        try {
            Optional<User> model = userRepositoryImpl.getByUserName(loginForm.username());
            if (model.isEmpty()) {
                throw new ResourceNotFoundException("user not found");
            }
            if (!(loginForm.password().equals(model.get().getPassword()))) {
                throw new AuthException("Invalid credentials", null);
            }
            return model.get().getRole();
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("User not found", e);
        } catch (DataAccessException e) {
            throw new DatabaseServiceException("Unknown error", e);
        }
    }

    @Override
    public UUID getIdByUsername(String username) {
        try {
            Optional<UUID> id = userRepositoryImpl.getIdByUserName(username);
            if (id.isPresent()) {
                return id.get();
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("User not found", e.getCause());
        }
        return null;
    }

    @Override
    public String getUsernameById(UUID userId) {
        try {
            return userRepositoryImpl.getUserNameById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("User not found");
        } catch (DataAccessException e) {
            throw new DatabaseServiceException("Unknown error occurred", e);
        }
    }
}
