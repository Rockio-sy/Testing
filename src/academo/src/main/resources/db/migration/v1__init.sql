-- V1__init.sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL,
    removed_at TIMESTAMP
);

CREATE TABLE task (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    student_id UUID,
    teacher_id UUID,
    removed_at TIMESTAMP,
    CONSTRAINT task_student_id_fkey FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT task_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES users(id)
);

CREATE TABLE submission (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    teacher_id UUID,
    student_id UUID,
    task_id UUID,
    answer TEXT NOT NULL,
    removed_at TIMESTAMP,
    CONSTRAINT submission_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES users(id),
    CONSTRAINT submission_student_id_fkey FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT submission_task_id_fkey FOREIGN KEY (task_id) REFERENCES task(id),
    CONSTRAINT unique_student_task_submission UNIQUE (student_id, task_id)
);

CREATE TABLE grade (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    value DOUBLE PRECISION NOT NULL,
    feedback VARCHAR(500),
    submission_id UUID,
    removed_at TIMESTAMP,
    CONSTRAINT grade_submission_id_fkey FOREIGN KEY (submission_id) REFERENCES submission(id)
);
