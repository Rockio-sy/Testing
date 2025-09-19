package org.academo.academo.repository;

import org.academo.academo.model.Question;

import java.util.List;

public interface QuestionRepository {
    List<Question> getAll();
    void save(Question question);
}
