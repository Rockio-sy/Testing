package org.academo.academo.builders;

import org.academo.academo.dto.TaskDTO;
import org.academo.academo.model.Task;

import java.util.UUID;

public class TaskBuilder {
    private UUID id = UUID.fromString("00000000-0000-0000-0000-00000000A001");
    private String title = "valid title";
    private String description = "Valid description";
    private UUID studentId = UUID.fromString("00000000-0000-0000-0000-00000000B001");
    private UUID teacherId = UUID.fromString("00000000-0000-0000-0000-00000000C001");

    public static TaskBuilder aTask() {
        return new TaskBuilder();
    }

    public TaskBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TaskBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder withStudentId(UUID studentId) {
        this.studentId = studentId;
        return this;
    }

    public TaskBuilder withTeacherId(UUID teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    public Task asEntity() {
        return new Task(id, title, description, studentId, teacherId);
    }

    public TaskDTO asDto() {
        return new TaskDTO(id, title, description, studentId, teacherId);
    }
}
