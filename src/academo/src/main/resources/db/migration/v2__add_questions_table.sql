CREATE TABLE questions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID REFERENCES users(id),  -- The student who asked the question
    question TEXT NOT NULL,                 -- The actual question
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp when the question was created
);

CREATE TABLE answers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    question_id UUID REFERENCES questions(id) ON DELETE CASCADE,  -- Link to the question being answered
    teacher_id UUID REFERENCES users(id),                        -- The teacher answering the question
    answer TEXT,                                                  -- The teacher's answer
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP               -- Timestamp when the answer was created
);
