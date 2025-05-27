package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task input = Task.builder()
                .userId(1L)
                .description("Test task")
                .targetDate(LocalDateTime.now().plusDays(1))
                .build();

        Task saved = input.toBuilder().id(1L).build();

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        Task result = taskService.createTask(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertFalse(result.isCompleted());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testGetAllTasksByUserId() {
        List<Task> tasks = List.of(
                new Task(1L, 1L, "Task 1", LocalDateTime.now(), null, false, false)
        );
        when(taskRepository.findAllByUserId(1L)).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByUserId(1L);

        assertEquals(1, result.size());
        verify(taskRepository).findAllByUserId(1L);
    }

    @Test
    void testGetPendingTasksByUserId() {
        List<Task> tasks = List.of(
                new Task(1L, 1L, "Active task", LocalDateTime.now(), null, false, false),
                new Task(2L, 1L, "Completed task", LocalDateTime.now(), null, true, false),
                new Task(3L, 1L, "Deleted task", LocalDateTime.now(), null, false, true)
        );

        when(taskRepository.findPendingByUserId(1L)).thenReturn(
                List.of(tasks.get(0)) // Только одна "живая" задача
        );

        List<Task> result = taskService.getPendingTasksByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("Active task", result.get(0).getDescription());
        verify(taskRepository).findPendingByUserId(1L);
    }

    @Test
    void testDeleteTask() {
        Long taskId = 42L;

        doNothing().when(taskRepository).softDelete(taskId);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).softDelete(taskId);
    }

}
