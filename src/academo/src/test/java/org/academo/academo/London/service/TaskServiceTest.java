package org.academo.academo.London.service;

import org.academo.academo.builders.TaskBuilder;
import org.academo.academo.dto.TaskDTO;
import org.academo.academo.Exception.AlreadyExistsException;
import org.academo.academo.Exception.DatabaseServiceException;
import org.academo.academo.Exception.InvalidDataException;
import org.academo.academo.Exception.ResourceNotFoundException;
import org.academo.academo.extension.TestWatcherExtension;
import org.academo.academo.model.Task;
import org.academo.academo.repository.impl.TaskRepositoryImpl;
import org.academo.academo.service.impl.TaskServiceImpl;
import org.academo.academo.util.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, TestWatcherExtension.class})
@Tag("unit")
@DisplayName("Task service test")
@TestMethodOrder(MethodOrderer.Random.class)
public class TaskServiceTest {

    @Mock
    private TaskRepositoryImpl taskRepository;

    @Mock
    private Converter converter;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDTO taskDTO;
    private final UUID studentTestId = UUID.randomUUID();
    private final UUID taskTestId = UUID.randomUUID();
    private final UUID teacherTestId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        var fixture = TaskBuilder
                .aTask()
                .withId(taskTestId)
                .withTitle("Math")
                .withStudentId(studentTestId)
                .withTeacherId(teacherTestId)
                .withDescription("Solve this task to get the idea");

        task = fixture.asEntity();
        taskDTO = fixture.asDto();
    }


    @Test
    @DisplayName("Create task - Success")
    void createTask_Successful() {
        when(converter.dtoToTask(any(TaskDTO.class))).thenReturn(task);
        doNothing().when(taskRepository).save(any(Task.class));

        taskService.createTask(taskDTO);

        verify(converter, times(1)).dtoToTask(taskDTO);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Get all tasks - Success")
    void getAllTasks_Success() {
        List<Task> tasks = Collections.singletonList(task);
        when(taskRepository.getAll()).thenReturn(tasks);
        when(converter.taskToDTO(any(Task.class))).thenReturn(taskDTO);

        List<TaskDTO> results = taskService.getAllTasks();

        assertEquals(1, results.size());
        assertEquals(taskDTO, results.get(0));
        verify(taskRepository, times(1)).getAll();
    }

    @Test
    @DisplayName("Get task by id -- success")
    void getTaskById_Success() {
        when(taskRepository.getById(any(UUID.class))).thenReturn(Optional.of(task));
        when(converter.taskToDTO(task)).thenReturn(taskDTO);

        TaskDTO dto = taskService.getById(UUID.randomUUID());

        assertEquals(dto, taskDTO);
    }

    @Test
    @DisplayName("Get all by user id -- success")
    void getAllByUserId_Success() {
        when(taskRepository.getAllByStudentId(any(UUID.class))).thenReturn(List.of(task));
        when(converter.taskToDTO(any())).thenReturn(taskDTO);

        List<TaskDTO> dtoList = taskService.getAllByUserId(UUID.randomUUID());
        assertEquals(dtoList.size(), 1);
    }

    @Test
    @DisplayName("Create task - Duplicate Key (AlreadyExistsException)")
    void createTask_DuplicateKey() {
        when(converter.dtoToTask(any(TaskDTO.class))).thenReturn(task);
        doThrow(DuplicateKeyException.class).when(taskRepository).save(any(Task.class));

        assertThrows(AlreadyExistsException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    @DisplayName("Create task - Invalid Data (InvalidDataException)")
    void createTask_InvalidData() {
        when(converter.dtoToTask(any(TaskDTO.class))).thenReturn(task);
        doThrow(DataIntegrityViolationException.class).when(taskRepository).save(any(Task.class));

        assertThrows(InvalidDataException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    @DisplayName("Get by ID - Task Not Found (ResourceNotFoundException)")
    void getById_NotFound() {
        when(taskRepository.getById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getById(taskTestId));
    }

    @Test
    @DisplayName("Get all by teacher ID - Empty List (ResourceNotFoundException)")
    void getAllByTeacherId_EmptyList() {
        when(taskRepository.getAllByTeacherId(any())).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getAllByTeacherId(teacherTestId));
    }

    @Test
    @DisplayName("Get all tasks - Database Error (DatabaseServiceException)")
    void getAllTasks_DatabaseError() {
        when(taskRepository.getAll()).thenThrow(new DataAccessException("Simulated DB error") {
        });

        assertThrows(DatabaseServiceException.class, () -> taskService.getAllTasks());
    }
}