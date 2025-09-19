package org.academo.academo.builders;

import org.academo.academo.dto.SubmissionDTO;
import org.academo.academo.model.Submission;

import java.util.UUID;

public class SubmissionBuilder {
    private UUID id = UUID.fromString("00000000-0000-0000-0000-00000000A001");
    private UUID teacherId = UUID.fromString("00000000-0000-0000-0000-00000000B001");
    private UUID studentId = UUID.fromString("00000000-0000-0000-0000-00000000C001");
    private UUID taskId = UUID.fromString("00000000-0000-0000-0000-00000000D001");
    private String answer = "You have to fix it";

    public static SubmissionBuilder aSubmission() {
        return new SubmissionBuilder();
    }

    public SubmissionBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public SubmissionBuilder withTeacherId(UUID teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public SubmissionBuilder withStudentId(UUID studentId) {
        this.studentId = studentId;
        return this;
    }

    public SubmissionBuilder withTaskId(UUID taskId) {
        this.taskId = taskId;
        return this;
    }

    public SubmissionBuilder withAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public Submission asEntity() {
        return new Submission(id, teacherId, studentId, taskId, answer);
    }

    public SubmissionDTO asDto() {
        return new SubmissionDTO(id, teacherId, studentId, taskId, answer);
    }
}
