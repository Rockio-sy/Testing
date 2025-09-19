package org.academo.academo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.academo.academo.dto.QuestionDTO;
import org.academo.academo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
@RequestMapping("/FAQ")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(){
        List<QuestionDTO> resp = questionService.getAll();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<String> postNewQuestion(@RequestBody QuestionDTO question){
        questionService.newQuestion(question);
        return new ResponseEntity<>("Question published", HttpStatus.CREATED);
    }

}
