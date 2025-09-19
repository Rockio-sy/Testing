package org.academo.academo.controller;

import org.academo.academo.dto.LoginForm;
import org.academo.academo.dto.UserDTO;
import org.academo.academo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access

@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginForm loginForm) {
        String role = userService.login(loginForm);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
