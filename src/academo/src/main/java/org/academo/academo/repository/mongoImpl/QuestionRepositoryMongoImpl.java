package org.academo.academo.repository.mongoImpl;

import org.academo.academo.model.Question;
import org.academo.academo.repository.QuestionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.database.type", havingValue = "mongodb")
public class QuestionRepositoryMongoImpl implements QuestionRepository {

    private final MongoTemplate mongoTemplate;

    public QuestionRepositoryMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Question> getAll() {
        return mongoTemplate.findAll(Question.class, "questions");
    }

    @Override
    public void save(Question question) {
        if (question.getId() == null) {
            question.setId(UUID.randomUUID());  // auto-generate ID if missing
        }
        mongoTemplate.save(question, "questions");
    }
}