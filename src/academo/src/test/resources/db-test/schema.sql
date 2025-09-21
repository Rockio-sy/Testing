-- schema-h2.sql
DROP TABLE IF EXISTS grade;
DROP TABLE IF EXISTS submission;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id UUID DEFAULT (RANDOM_UUID()) PRIMARY KEY,
  full_name VARCHAR(100) NOT NULL,
  username  VARCHAR(100) NOT NULL UNIQUE,
  password  VARCHAR(100) NOT NULL,
  role      VARCHAR(10)  NOT NULL,
  removed_at TIMESTAMP
);

CREATE TABLE task (
  id UUID DEFAULT (RANDOM_UUID()) PRIMARY KEY,
  title       VARCHAR(100) NOT NULL,
  description TEXT NOT NULL,
  student_id  UUID REFERENCES users(id),
  teacher_id  UUID REFERENCES users(id),
  removed_at  TIMESTAMP
);

CREATE TABLE submission (
  id UUID DEFAULT (RANDOM_UUID()) PRIMARY KEY,
  teacher_id  UUID REFERENCES users(id),
  student_id  UUID REFERENCES users(id),
  task_id     UUID REFERENCES task(id),
  answer      TEXT NOT NULL,
  removed_at  TIMESTAMP
);

CREATE TABLE grade (
  id UUID DEFAULT (RANDOM_UUID()) PRIMARY KEY,
  grade_value DOUBLE PRECISION NOT NULL,
  feedback VARCHAR(500),
  submission_id UUID REFERENCES submission(id),
  removed_at TIMESTAMP
);

