package org.academo.academo.ui;

import org.academo.academo.Exception.AlreadyExistsException;
import org.academo.academo.Exception.InvalidDataException;
import org.academo.academo.Exception.ResourceNotFoundException;
import org.academo.academo.dto.TaskDTO;
import org.academo.academo.service.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Profile("cli")
public class TaskTechTest {

    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private InputProvider inputProvider;

    // Handle Task Creation
    public void handleCreateTask() {
        TaskDTO task = inputProvider.inputTaskDTO(); // Fetch input from the user (mocked)

        try {
            taskService.createTask(task);
            System.out.println("✅ Task created successfully: \n" + task.toString());
        } catch (AlreadyExistsException e) {
            System.out.println("❌ Task with this identifier already exists.");
        } catch (InvalidDataException e) {
            System.out.println("❌ Invalid task data. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❗ Unexpected error: " + e.getMessage());
        }
    }

    // Handle Fetching All Tasks
    public void handleGetAllTasks() {
        try {
            List<TaskDTO> tasks = taskService.getAllTasks();
            if (tasks.isEmpty()) {
                System.out.println("⚠️ No tasks found.");
            } else {
                System.out.println("✅ List of all tasks:");
                for (TaskDTO task : tasks) {
                    System.out.println(task.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("❗ Error fetching tasks: " + e.getMessage());
        }
    }

    // Handle Fetching Tasks by Teacher ID
    public void handleGetTasksByTeacherId() {
        UUID teacherId = inputProvider.promptUUID("Enter teacher UUID to fetch tasks");

        try {
            List<TaskDTO> tasks = taskService.getAllByTeacherId(teacherId);
            if (tasks.isEmpty()) {
                System.out.println("⚠️ No tasks found for this teacher.");
            } else {
                System.out.println("✅ List of tasks for teacher ID: " + teacherId);
                for (TaskDTO task : tasks) {
                    System.out.println(task.toString());
                }
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ No tasks found for teacher ID: " + teacherId);
        } catch (Exception e) {
            System.out.println("❗ Error fetching tasks: " + e.getMessage());
        }
    }

    // Handle Fetching Task by ID
    public void handleGetTaskById() {
        UUID taskId = inputProvider.promptUUID("Enter task UUID to fetch");

        try {
            TaskDTO task = taskService.getById(taskId);
            System.out.println("✅ Task found: " + task.toString());
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ Task not found with ID: " + taskId);
        } catch (Exception e) {
            System.out.println("❗ Error fetching task: " + e.getMessage());
        }
    }

    // Handle Fetching Tasks by User ID
    public void handleGetTasksByUserId() {
        UUID userId = inputProvider.promptUUID("Enter user UUID to fetch tasks");

        try {
            List<TaskDTO> tasks = taskService.getAllByUserId(userId);
            if (tasks.isEmpty()) {
                System.out.println("⚠️ No tasks found for this user.");
            } else {
                System.out.println("✅ List of tasks for user ID: " + userId);
                for (TaskDTO task : tasks) {
                    System.out.println(task.toString());
                }
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ No tasks found for user ID: " + userId);
        } catch (Exception e) {
            System.out.println("❗ Error fetching tasks: " + e.getMessage());
        }
    }
}
