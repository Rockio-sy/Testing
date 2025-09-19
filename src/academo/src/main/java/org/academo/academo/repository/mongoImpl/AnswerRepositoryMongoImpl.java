package org.academo.academo.repository.mongoImpl;

import org.academo.academo.model.Answer;
import org.academo.academo.repository.AnswerRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.database.type", havingValue = "mongodb")
public class AnswerRepositoryMongoImpl implements AnswerRepository {

    private final MongoTemplate mongoTemplate;

    public AnswerRepositoryMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Answer getByrQuestionId(UUID questionID) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("questionID").is(questionID)),
                Answer.class,
                "answers"
        );
    }

    @Override
    public void save(Answer answer) {
        if (answer.getId() == null) {
            answer.setId(UUID.randomUUID());
        }
        mongoTemplate.save(answer, "answers");
    }
}