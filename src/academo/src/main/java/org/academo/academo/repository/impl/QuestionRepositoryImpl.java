package org.academo.academo.repository.impl;

import org.academo.academo.model.Question;
import org.academo.academo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
//@ConditionalOnProperty(name = "app.database.type", havingValue = "postgres")
public class QuestionRepositoryImpl implements QuestionRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Question> getAll() {
        String sql = "SELECT * FROM questions;";
        return jdbcTemplate.query(sql, new QuestionRowMapper());
    }

    @Override
    public void save(Question question) {
        String sql = "INSERT INTO questions (student_id, question)" +
                " VALUES " +
                "(?, ?)";
        jdbcTemplate.update(sql, question.getStudentID(), question.getQuestion());
    }

    public class QuestionRowMapper implements RowMapper<Question> {
        @Override
        public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
            Question question = new Question();
            question.setId((UUID) rs.getObject("id"));
            question.setQuestion(rs.getString("question"));
            question.setStudentID((UUID) rs.getObject("student_id"));
            return question;
        }
    }
}
