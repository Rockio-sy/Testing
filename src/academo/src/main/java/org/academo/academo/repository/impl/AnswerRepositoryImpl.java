package org.academo.academo.repository.impl;

import org.academo.academo.model.Answer;
import org.academo.academo.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
//@ConditionalOnProperty(name = "app.database.type", havingValue = "postgres")
public class AnswerRepositoryImpl implements AnswerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Answer getByrQuestionId(UUID questionID) {
        String sql = "SELECT * FROM answers WHERE question_id = ?";
        return jdbcTemplate.queryForObject(sql, new AnswerRowMapper(), questionID);
    }

    @Override
    public void save(Answer answer) {
        String sql = "INSERT INTO answers(question_id, teacher_id, answer)" +
                " VALUES " +
                "(?, ?, ?)";
        jdbcTemplate.update(sql, answer.getQuestionID(), answer.getTeacherID(), answer.getAnswer());
    }

    private class AnswerRowMapper implements RowMapper<Answer> {
        @Override
        public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Answer answer = new Answer();
            answer.setId((UUID)rs.getObject("id"));
            answer.setAnswer(rs.getString("answer"));
            answer.setQuestionID((UUID) rs.getObject("question_id"));
            answer.setTeacherID((UUID) rs.getObject("teacher_id"));
            return answer;
        }
    }
}
