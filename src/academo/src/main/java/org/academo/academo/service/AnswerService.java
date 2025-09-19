package org.academo.academo.service;

import org.academo.academo.dto.AnswerDTO;
import org.academo.academo.model.Answer;

import java.util.UUID;

public interface AnswerService {

    AnswerDTO getForQuestion(UUID questionID);

    void answerQuestion(AnswerDTO answer);
}
