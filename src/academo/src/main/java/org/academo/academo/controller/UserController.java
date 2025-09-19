package org.academo.academo.controller;

import org.academo.academo.dto.UserDTO;
import org.academo.academo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/new")
    public ResponseEntity<String> createNewUser(@RequestBody UserDTO form) {
        userService.createUser(form);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> removeUser(@RequestParam UUID id) {
        userService.removeUser(id);
        return new ResponseEntity<>("Removed", HttpStatus.OK);
    }

    @GetMapping("/get-id")
    public ResponseEntity<Map<String, String>> getIdByUserName(@RequestParam String username) {
        UUID id = userService.getIdByUsername(username);
        return new ResponseEntity<>(Map.of("id", id.toString()), HttpStatus.OK);
    }

    @GetMapping("get-username")
    public ResponseEntity<String> getUsernameById(@RequestParam UUID id) {
        return new ResponseEntity<>(userService.getUsernameById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAll(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
