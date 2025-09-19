package org.academo.academo.controller;

import org.academo.academo.dto.SubmissionDTO;
import org.academo.academo.service.impl.SubmissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access

@RequestMapping("/submit")
public class SubmissionController {
    @Autowired
    private SubmissionServiceImpl submissionService;

    @PostMapping("/new")
    public ResponseEntity<String> doSubmit(@RequestBody SubmissionDTO form) {
        submissionService.submit(form);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubmissionDTO>> getAll() {
        return new ResponseEntity<>(submissionService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/one")
    public ResponseEntity<SubmissionDTO> getOneById(@RequestParam String id) {
        return new ResponseEntity<>(submissionService.getOneById(UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping("by-task")
    public ResponseEntity<SubmissionDTO> getByTaskId(@RequestParam String taskId) {
        return new ResponseEntity<>(submissionService.getByTaskId(UUID.fromString(taskId)), HttpStatus.OK);
    }
}
