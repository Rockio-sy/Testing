package org.academo.academo.ui;

import org.academo.academo.dto.GradeDTO;
import org.academo.academo.dto.SubmissionDTO;
import org.academo.academo.dto.TaskDTO;
import org.academo.academo.dto.UserDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.UUID;

@Component
@Profile("cli")
public class InputProvider {
    private static final Scanner read = new Scanner(System.in);


    public String promptString(String label) {
        while (true) {
            System.out.print(label + ": ");
            String input = read.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("❌ Input is required.");
        }
    }

    public double promptDouble(String label) {
        while (true) {
            System.out.print(label + ": ");
            try {
                return Double.parseDouble(read.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }

    public UUID promptUUID(String label) {
        while (true) {
            System.out.print(label + ": ");
            String input = read.nextLine().trim();
            try {
                return UUID.fromString(input);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid UUID format. Please try again.");
            }
        }
    }

    public UserDTO inputUserDTO() {
        UserDTO user = new UserDTO();
        user.setUsername(promptString("Enter username"));
        user.setPassword(promptString("Enter password"));
        user.setFullName(promptString("Enter full name"));
        user.setRole(promptString("Enter role (e.g. TEACHER, STUDENT)"));
        return user;
    }
    public TaskDTO inputTaskDTO() {
        TaskDTO task = new TaskDTO();
        task.setTitle(promptString("Enter task title"));
        task.setDescription(promptString("Enter task description"));
        task.setStudentId(promptUUID("Enter student UUID"));
        task.setTeacherId(promptUUID("Enter teacher UUID"));
        return task;
    }
    public SubmissionDTO inputSubmissionDTO() {
        SubmissionDTO submission = new SubmissionDTO();
        submission.setTeacherId(promptUUID("Enter teacher UUID"));
        submission.setStudentId(promptUUID("Enter student UUID"));
        submission.setTaskId(promptUUID("Enter task UUID"));
        submission.setAnswer(promptString("Enter answer"));
        return submission;
    }
    public GradeDTO inputGradeDTO() {
        GradeDTO grade = new GradeDTO();
        grade.setValue(promptDouble("Enter grade value (e.g. 95.0)"));
        grade.setFeedback(promptString("Enter feedback"));
        grade.setSubmissionId(promptUUID("Enter submission UUID"));
        return grade;
    }

}

