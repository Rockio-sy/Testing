package org.academo.academo.repository.mongoImpl;


import org.academo.academo.model.User;
import org.academo.academo.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.database.type", havingValue = "mongodb")
public class UserRepositoryMongoImpl implements UserRepository {

    private final MongoTemplate mongoTemplate;

    public UserRepositoryMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        mongoTemplate.save(user, "users");
    }

    @Override
    public void removeUser(UUID userId) {
        User user = mongoTemplate.findById(userId, User.class, "users");
        if (user != null) {
            mongoTemplate.save(user, "users");
        }
    }

    @Override
    public List<User> getAll() {
        return mongoTemplate.find(
                Query.query(Criteria.where("removedAt").exists(false).orOperator(
                        Criteria.where("removedAt").is(null))),
                User.class,
                "users"
        );
    }

    @Override
    public Optional<User> getByUserId(UUID userId) {
        return Optional.ofNullable(mongoTemplate.findById(userId, User.class, "users"));
    }

    @Override
    public Optional<User> getByUserName(String username) {
        return Optional.ofNullable(mongoTemplate.findOne(
                Query.query(Criteria.where("username").is(username)
                        .and("removedAt").exists(false).orOperator(
                                Criteria.where("removedAt").is(null))),
                User.class,
                "users"
        ));
    }

    @Override
    public Optional<UUID> getIdByUserName(String username) {
        User user = mongoTemplate.findOne(
                Query.query(Criteria.where("username").is(username)),
                User.class,
                "users"
        );
        return user != null ? Optional.of(user.getId()) : Optional.empty();
    }

    @Override
    public String getUserNameById(UUID userId) {
        User user = mongoTemplate.findById(userId, User.class, "users");
        return user != null ? user.getUsername() : null;
    }
}