package org.academo.academo.service;

import org.academo.academo.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {
    List<QuestionDTO> getAll();
    void newQuestion(QuestionDTO question);
}
