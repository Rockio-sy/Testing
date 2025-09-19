package org.academo.academo.repository;

import org.academo.academo.dto.AnswerDTO;
import org.academo.academo.model.Answer;

import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository {
    Answer getByrQuestionId(UUID questionID);
    void save(Answer answer);


}
