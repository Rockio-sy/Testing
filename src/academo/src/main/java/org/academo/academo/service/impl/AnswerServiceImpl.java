package org.academo.academo.service.impl;

import org.academo.academo.Exception.DatabaseServiceException;
import org.academo.academo.Exception.ResourceNotFoundException;
import org.academo.academo.dto.AnswerDTO;
import org.academo.academo.model.Answer;
import org.academo.academo.repository.AnswerRepository;
import org.academo.academo.service.AnswerService;
import org.academo.academo.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository repository;
    @Autowired
    private Converter converter;

    @Override
    public AnswerDTO getForQuestion(UUID questionID) {
        try {
            Answer model = repository.getByrQuestionId(questionID);
            return converter.answerToDTO(model);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Not found");
        }
    }

    @Override
    public void answerQuestion(AnswerDTO answer) {
        Answer model = converter.DTOtoAnswer(answer);
        try {
            repository.save(model);
        } catch (DataAccessException e) {
            throw new DatabaseServiceException("Unknown error", e);
        }
    }
}
