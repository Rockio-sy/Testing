package org.academo.academo.util;

import org.academo.academo.dto.*;
import org.academo.academo.model.*;
import org.springframework.stereotype.Component;

@Component
public class Converter {
    public User DTOtoUser(UserDTO dto) {
        User model = new User();
        model.setFullName(dto.getFullName());
        model.setPassword(dto.getPassword());
        model.setUsername(dto.getUsername());
        model.setRole(dto.getRole());
        return model;
    }

    public UserDTO userToDTO(User model) {
        UserDTO dto = new UserDTO();
        dto.setId(model.getId());
        dto.setFullName(model.getFullName());
        dto.setUsername(model.getUsername());
        dto.setRole(model.getRole());
        return dto;
    }

    public Task dtoToTask(TaskDTO task) {
        Task model = new Task();
        model.setTitle(task.getTitle());
        model.setDescription(task.getDescription());
        model.setTeacherId(task.getTeacherId());
        model.setStudentId(task.getStudentId());
        return model;
    }

    public TaskDTO taskToDTO(Task model) {
        TaskDTO dto = new TaskDTO();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setTeacherId(model.getTeacherId());
        dto.setStudentId(model.getStudentId());
        return dto;
    }

    public Submission DTOtoSubmission(SubmissionDTO dto) {
        Submission model = new Submission();
        model.setId(dto.getId());
        model.setTeacherId(dto.getTeacherId());
        model.setStudentId(dto.getStudentId());
        model.setTaskId(dto.getTaskId());
        model.setAnswer(dto.getAnswer());
        return model;
    }

    public SubmissionDTO submissionToDTO(Submission model) {
        SubmissionDTO dto = new SubmissionDTO();
        dto.setId(model.getId());
        dto.setTeacherId(model.getTeacherId());
        dto.setStudentId(model.getStudentId());
        dto.setTaskId(model.getTaskId());
        dto.setAnswer(model.getAnswer());
        return dto;
    }

    public Grade DTOtoGrade(GradeDTO dto) {
        Grade model = new Grade();
        model.setValue(dto.getValue());
        model.setFeedback(dto.getFeedback());
        model.setSubmissionId(dto.getSubmissionId());
        return model;
    }

    public GradeDTO gradeToDTO(Grade model) {
        GradeDTO dto = new GradeDTO();
        dto.setId(model.getId());
        dto.setValue(model.getValue());
        dto.setFeedback(model.getFeedback());
        dto.setSubmissionId(model.getSubmissionId());
        return dto;
    }


    // Convert AnswerDTO to Answer entity
    public Answer DTOtoAnswer(AnswerDTO dto) {
        Answer model = new Answer();
        model.setQuestionID(dto.getQuestionID());
        model.setTeacherID(dto.getTeacherID());
        model.setAnswer(dto.getAnswer());
        return model;
    }

    // Convert Answer entity to AnswerDTO
    public AnswerDTO answerToDTO(Answer model) {
        AnswerDTO dto = new AnswerDTO();
        dto.setId(model.getId());
        dto.setQuestionID(model.getQuestionID());
        dto.setTeacherID(model.getTeacherID());
        dto.setAnswer(model.getAnswer());
        return dto;
    }

    // Convert QuestionDTO to Question entity
    public Question DTOtoQuestion(QuestionDTO dto) {
        Question model = new Question();
        model.setStudentID(dto.getStudentID());
        model.setQuestion(dto.getQuestion());
        return model;
    }

    // Convert Question entity to QuestionDTO
    public QuestionDTO questionToDTO(Question model) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(model.getId());
        dto.setStudentID(model.getStudentID());
        dto.setQuestion(model.getQuestion());
        return dto;
    }

}
