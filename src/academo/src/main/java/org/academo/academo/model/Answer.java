package org.academo.academo.model;

import java.util.UUID;

public class Answer {
    private UUID id;
    private UUID questionID;
    private UUID teacherID;
    private String answer;

    public Answer() {
    }

    public Answer(UUID id, UUID questionID, UUID teacherID, String answer) {
        this.id = id;
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
