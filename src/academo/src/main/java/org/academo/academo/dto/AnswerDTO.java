package org.academo.academo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AnswerDTO {
    private UUID id;
    @NotNull(message = "Question id is required")
    private UUID questionID;
    @NotNull(message = "Teacher id is required")
    private UUID teacherID;

    @NotBlank(message = "Answer is empty")
    private String answer;

    public AnswerDTO() {
    }

    public AnswerDTO(UUID questionID, UUID teacherID, String answer) {
        this.questionID = questionID;
        this.teacherID = teacherID;
        this.answer = answer;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getQuestionID() {
        return questionID;
    }

    public void setQuestionID(UUID questionID) {
        this.questionID = questionID;
    }

    public UUID getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(UUID teacherID) {
        this.teacherID = teacherID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
