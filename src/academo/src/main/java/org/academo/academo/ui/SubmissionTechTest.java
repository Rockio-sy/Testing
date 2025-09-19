package org.academo.academo.ui;

import org.academo.academo.Exception.AlreadyExistsException;
import org.academo.academo.Exception.DatabaseServiceException;
import org.academo.academo.Exception.InvalidDataException;
import org.academo.academo.Exception.ResourceNotFoundException;
import org.academo.academo.dto.SubmissionDTO;
import org.academo.academo.service.impl.SubmissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("cli")
public class SubmissionTechTest {

    @Autowired
    private SubmissionServiceImpl submissionService;
    @Autowired
    private InputProvider inputProvider;

    // Handle Submission Creation
    public void handleSubmitSubmission() {
        SubmissionDTO submission = inputProvider.inputSubmissionDTO(); // Fetch input (mocked)

        try {
            submissionService.submit(submission);
            System.out.println("✅ Submission created successfully for task ID: " + submission.getTaskId());
        } catch (AlreadyExistsException e) {
            System.out.println("❌ Submission for this task already exists.");
        } catch (InvalidDataException e) {
            System.out.println("❌ Invalid submission data. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❗ Unexpected error: " + e.getMessage());
        }
    }

    // Handle Fetching Submission by Task ID
    public void handleGetSubmissionByTaskId() {
        UUID taskId = inputProvider.promptUUID("Enter task UUID to fetch submission");

        try {
            SubmissionDTO submission = submissionService.getByTaskId(taskId);
            System.out.println(submission.toString());
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ Submission not found for task ID: " + taskId);
        } catch (Exception e) {
            System.out.println("❗ Unexpected error: " + e.getMessage());
        }
    }
}
