package org.academo.academo.model;

import java.util.UUID;

public class Question {
    private UUID id;
    private UUID studentID;
    private String question;

    public Question() {
    }


    public Question(UUID id, UUID studentID, String question) {
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
