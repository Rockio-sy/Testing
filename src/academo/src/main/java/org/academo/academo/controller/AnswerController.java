package org.academo.academo.controller;

import org.academo.academo.dto.AnswerDTO;
import org.academo.academo.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
@RequestMapping("/answer")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @GetMapping("/for")
    public ResponseEntity<AnswerDTO> getAnswer(@RequestParam UUID questionID) {
        return new ResponseEntity<>(answerService.getForQuestion(questionID), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<String> answerQuestion(@RequestBody  AnswerDTO answer) {
        answerService.answerQuestion(answer);
        return new ResponseEntity<>("Answer Created", HttpStatus.CREATED);
    }

}
