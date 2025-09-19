package org.academo.academo.service.impl;

import org.academo.academo.Exception.DatabaseServiceException;
import org.academo.academo.Exception.ResourceNotFoundException;
import org.academo.academo.dto.QuestionDTO;
import org.academo.academo.model.Question;
import org.academo.academo.repository.QuestionRepository;
import org.academo.academo.service.QuestionService;
import org.academo.academo.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository repository;
    @Autowired
    private Converter converter;

    @Override
    public List<QuestionDTO> getAll() {
        try {
            List<Question> models = repository.getAll();

            List<QuestionDTO> resp = new ArrayList<>();
            for (Question q : models) {
                QuestionDTO dto = converter.questionToDTO(q);
                resp.add(dto);
            }

            return resp;
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("No questions found", e);
        }

    }


    @Override
    public void newQuestion(QuestionDTO question) {
        Question model = converter.DTOtoQuestion(question);
        try {
            repository.save(model);
        } catch (DataAccessException e) {
            throw new DatabaseServiceException("unknown error", e);
        }
    }
}
