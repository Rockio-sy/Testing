package org.academo.academo.repository.mongoImpl;


import com.jayway.jsonpath.Criteria;
import org.academo.academo.model.Task;
import org.academo.academo.repository.TaskRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.database.type", havingValue = "mongodb")
public class TaskRepositoryMongoImpl implements TaskRepository {

    private final MongoTemplate mongoTemplate;

    public TaskRepositoryMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(Task task) {
        if (task.getId() == null) {
            task.setId(UUID.randomUUID());
        }
        mongoTemplate.save(task, "task");
    }

    @Override
    public Optional<UUID> getIdByTaskTitle(String title) {
        Task task = mongoTemplate.findOne(
                Query.query((CriteriaDefinition) Criteria.where("title").is(title)),
                Task.class,
                "task"
        );
        return task != null ? Optional.of(task.getId()) : Optional.empty();
    }

    @Override
    public List<Task> getAll() {
        return mongoTemplate.findAll(Task.class, "task");
    }

    @Override
    public Optional<Task> getById(UUID taskId) {
        return Optional.ofNullable(mongoTemplate.findById(taskId, Task.class, "task"));
    }

    @Override
    public List<Task> getAllByStudentId(UUID studentId) {
        return mongoTemplate.find(
                Query.query((CriteriaDefinition) Criteria.where("studentId").is(studentId)),
                Task.class,
                "task"
        );
    }

    @Override
    public List<Task> getAllByTeacherId(UUID teacherId) {
        return mongoTemplate.find(
                Query.query((CriteriaDefinition) Criteria.where("teacherId").is(teacherId)),
                Task.class,
                "task"
        );
    }
}