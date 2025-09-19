package org.academo.academo.repository.mongoImpl;

import org.academo.academo.model.Submission;
import org.academo.academo.repository.SubmissionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.database.type", havingValue = "mongodb")
public class SubmissionRepositoryMongoImpl implements SubmissionRepository {

    private final MongoTemplate mongoTemplate;

    public SubmissionRepositoryMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(Submission submission) {
        if (submission.getId() == null) {
            submission.setId(UUID.randomUUID());
        }
        mongoTemplate.save(submission, "submission");
    }

    @Override
    public List<Submission> getAll() {
        return mongoTemplate.findAll(Submission.class, "submission");
    }

    @Override
    public Optional<Submission> getByTaskId(UUID taskId) {
        return Optional.ofNullable(mongoTemplate.findById(taskId, Submission.class, "submission"));
    }

    @Override
    public Optional<UUID> getIdByTaskId(UUID taskId) {
        Submission submission = mongoTemplate.findById(taskId, Submission.class, "submission");
        return submission != null ? Optional.of(submission.getId()) : Optional.empty();
    }

    @Override
    public Submission getById(UUID id) {
        return mongoTemplate.findById(id, Submission.class, "submission");
    }
}