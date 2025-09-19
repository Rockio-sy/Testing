package org.academo.academo.controller;

import org.academo.academo.dto.TaskDTO;
import org.academo.academo.service.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskServiceImpl taskService;

    @PostMapping("/new")
    public ResponseEntity<String> createNewTask(@RequestBody TaskDTO form) {
        taskService.createTask(form);
        return new ResponseEntity<>("New task Created", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/one")
    public ResponseEntity<TaskDTO> getOneTaskById(UUID id) {
        TaskDTO resp = taskService.getById(id);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<String> getTitle(UUID id) {
        return new ResponseEntity<>((taskService.getById(id)).getTitle(), HttpStatus.OK);
    }
}
