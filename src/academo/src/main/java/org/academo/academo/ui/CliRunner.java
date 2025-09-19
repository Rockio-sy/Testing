package org.academo.academo.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@Profile("cli")
public class CliRunner implements CommandLineRunner {

    @Autowired
    private UserTechTest userTechTest;

    @Autowired
    private TaskTechTest taskTechTest;

    @Autowired
    private SubmissionTechTest submissionTechTest;

    @Autowired
    private GradeTechTest gradeTechTest;

    @Autowired
    private ApplicationContext application;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Welcome message
        System.out.println("Welcome to the Management CLI");

        while (running) {
            // Display main menu options
            System.out.println("\nPlease choose an option:");
            System.out.println("1. User Management");
            System.out.println("2. Task Management");
            System.out.println("3. Submission Management");
            System.out.println("4. Grade Management");
            System.out.println("5. Exit");

            // Get user input
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleUserManagement(scanner);
                    break;
                case "2":
                    handleTaskManagement(scanner);
                    break;
                case "3":
                    handleSubmissionManagement(scanner);
                    break;
                case "4":
                    handleGradeManagement(scanner);
                    break;
                case "5":
                    running = false; // Exit loop
                    System.out.println("Exiting. Goodbye!");
                    SpringApplication.exit(application);  // Graceful shutdown of Spring app
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose a valid option.");
            }
        }

        scanner.close();
    }

    // User Management Menu
    private void handleUserManagement(Scanner scanner) {
        boolean userRunning = true;
        while (userRunning) {
            System.out.println("\nUser Management Options:");
            System.out.println("1. Create a new user");
            System.out.println("2. Remove a user");
            System.out.println("3. Get all users");
            System.out.println("4. Get user by ID");
            System.out.println("5. Back to Main Menu");

            String userChoice = scanner.nextLine();

            switch (userChoice) {
                case "1":
                    userTechTest.handleCreateUser();
                    break;
                case "2":
                    userTechTest.handleRemoveUser();
                    break;
                case "3":
                    userTechTest.handleGetAllUsers();
                    break;
                case "4":
                    userTechTest.handleGetUserById();
                    break;
                case "5":
                    userRunning = false;
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose a valid option.");
            }
        }
    }

    // Task Management Menu
    private void handleTaskManagement(Scanner scanner) {
        boolean taskRunning = true;
        while (taskRunning) {
            System.out.println("\nTask Management Options:");
            System.out.println("1. Create a new task");
            System.out.println("2. Get all tasks");
            System.out.println("3. Get tasks by teacher ID");
            System.out.println("4. Get task by ID");
            System.out.println("5. Get tasks by user ID");
            System.out.println("6. Back to Main Menu");

            String taskChoice = scanner.nextLine();

            switch (taskChoice) {
                case "1":
                    taskTechTest.handleCreateTask();
                    break;
                case "2":
                    taskTechTest.handleGetAllTasks();
                    break;
                case "3":
                    taskTechTest.handleGetTasksByTeacherId();
                    break;
                case "4":
                    taskTechTest.handleGetTaskById();
                    break;
                case "5":
                    taskTechTest.handleGetTasksByUserId();
                    break;
                case "6":
                    taskRunning = false;
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose a valid option.");
            }
        }
    }

    // Submission Management Menu
    private void handleSubmissionManagement(Scanner scanner) {
        boolean submissionRunning = true;
        while (submissionRunning) {
            System.out.println("\nSubmission Management Options:");
            System.out.println("1. Submit a submission");
            System.out.println("2. Get submission by task ID");
            System.out.println("3. Back to Main Menu");

            String submissionChoice = scanner.nextLine();

            switch (submissionChoice) {
                case "1":
                    submissionTechTest.handleSubmitSubmission();
                    break;
                case "2":
                    submissionTechTest.handleGetSubmissionByTaskId();
                    break;
                case "3":
                    submissionRunning = false;
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose a valid option.");
            }
        }
    }

    // Grade Management Menu
    private void handleGradeManagement(Scanner scanner) {
        boolean gradeRunning = true;
        while (gradeRunning) {
            System.out.println("\nGrade Management Options:");
            System.out.println("1. Create a new grade");
            System.out.println("2. Get grade by submission ID");
            System.out.println("3. Back to Main Menu");

            String gradeChoice = scanner.nextLine();

            switch (gradeChoice) {
                case "1":
                    gradeTechTest.handleCreateGrade();
                    break;
                case "2":
                    gradeTechTest.handleGetGradeBySubmissionId();
                    break;
                case "3":
                    gradeRunning = false;
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose a valid option.");
            }
        }
    }
}
