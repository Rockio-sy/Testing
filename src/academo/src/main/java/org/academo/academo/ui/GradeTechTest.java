package org.academo.academo.ui;


import org.academo.academo.Exception.AlreadyExistsException;
import org.academo.academo.Exception.InvalidDataException;
import org.academo.academo.Exception.ResourceNotFoundException;
import org.academo.academo.dto.GradeDTO;
import org.academo.academo.service.impl.GradeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("cli")
public class GradeTechTest {

    @Autowired
    private GradeServiceImpl gradeService;
    @Autowired
    private InputProvider inputProvider;

    // Handle Grade Creation
    public void handleCreateGrade() {
        GradeDTO grade = inputProvider.inputGradeDTO(); // Assume this method fetches input

        try {
            gradeService.createGrade(grade);
            System.out.println("✅ Grade created successfully for submission ID: " + grade.getSubmissionId());
        } catch (AlreadyExistsException e) {
            System.out.println("❌ Grade for this submission already exists.");
        } catch (InvalidDataException e) {
            System.out.println("❌ Invalid grade data. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❗ Unexpected error: " + e.getMessage());
        }
    }

    // Handle Grade Fetch by Submission ID
    public void handleGetGradeBySubmissionId() {
        UUID submissionId = inputProvider.promptUUID("Enter submission UUID to fetch grade");

        try {
            GradeDTO grade = gradeService.getBySubmissionId(submissionId);
            System.out.println("✅ Grade found for submission ID: " + grade.toString());
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ Grade not found for submission ID: " + submissionId);
        } catch (Exception e) {
            System.out.println("❗ Unexpected error: " + e.getMessage());
        }
    }
}
