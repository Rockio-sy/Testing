package org.academo.academo.controller;

import org.academo.academo.dto.GradeDTO;
import org.academo.academo.service.impl.GradeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access

@RequestMapping("/grade")
public class GradeController {
    @Autowired
    private GradeServiceImpl gradeService;

    @GetMapping("/one")
    public ResponseEntity<GradeDTO> getGradeBySubmissionId(UUID id) {
        return new ResponseEntity<>(gradeService.getBySubmissionId(id), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<String> GradeSubmission(@RequestBody GradeDTO form) {
        gradeService.createGrade(form);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

}
