package org.academo.academo.Classic.repository;

import org.academo.academo.repository.impl.GradeRepositoryImpl;
import org.academo.academo.repository.impl.SubmissionRepositoryImpl;
import org.academo.academo.repository.impl.TaskRepositoryImpl;
import org.academo.academo.repository.impl.UserRepositoryImpl;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql({"classpath:db-test/schema.sql"})
@Sql(value = "classpath:db-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import({
        UserRepositoryImpl.class,
        GradeRepositoryImpl.class,
        SubmissionRepositoryImpl.class,
        TaskRepositoryImpl.class
})
public abstract class BaseRepositoryTest {
}



