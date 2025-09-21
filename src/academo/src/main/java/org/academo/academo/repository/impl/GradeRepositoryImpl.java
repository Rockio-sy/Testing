package org.academo.academo.repository.impl;

import org.academo.academo.model.Grade;
import org.academo.academo.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.database.type", havingValue = "postgres")
public class GradeRepositoryImpl implements GradeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Grade grade) {
        // Quote the column name so it works with H2 ("value") and Postgres
        String sql = "INSERT INTO grade (\"value\", feedback, submission_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, grade.getValue(), grade.getFeedback(), grade.getSubmissionId());
    }

    @Override
    public Optional<Grade> getById(UUID id) {
        String sql = "SELECT * FROM grade WHERE id = ?";
        return Optional.of(jdbcTemplate.queryForObject(sql, new GradeRowMapper(), id));
    }

    @Override
    public List<Grade> getAll() {
        String sql = "SELECT * FROM grade;";
        return jdbcTemplate.query(sql, new GradeRowMapper());
    }

    @Override
    public Optional<Grade> getBySubmissionId(UUID submissionId) {
        String sql = "SELECT * FROM grade WHERE submission_id = ?";
        return Optional.of(jdbcTemplate.queryForObject(sql, new GradeRowMapper(), submissionId));
    }

    private class GradeRowMapper implements RowMapper<Grade> {
        @Override
        public Grade mapRow(ResultSet rs, int rowNum) throws SQLException {
            Grade grade = new Grade();
            grade.setId((UUID) rs.getObject("id"));
            // Prefer getDouble over getFloat->double cast
            grade.setValue(rs.getDouble("value"));
            grade.setFeedback(rs.getString("feedback"));
            grade.setSubmissionId((UUID) rs.getObject("submission_id"));
            return grade;
        }
    }
}
