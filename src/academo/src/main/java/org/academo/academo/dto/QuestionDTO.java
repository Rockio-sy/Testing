package org.academo.academo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class QuestionDTO {
    private UUID id;
    @NotNull(message = "Student ID cannot be null")
    private UUID studentID;
    @NotBlank(message = "Question is blank")
    private String question;

    public QuestionDTO() {
    }

    public QuestionDTO(UUID studentID, String question) {
        this.studentID = studentID;
        this.question = question;
    }

    public QuestionDTO(UUID id, UUID teacherId, UUID studentID, String question, String answer) {
        this.id = id;
        this.studentID = studentID;
        this.question = question;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStudentID() {
        return studentID;
    }

    public void setStudentID(UUID studentID) {
        this.studentID = studentID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
