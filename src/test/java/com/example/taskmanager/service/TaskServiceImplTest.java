package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertFalse(result.isCompleted());
        assertFalse(result.isDeleted());
        verify(taskRepository).save(task);
    }

    @Test
    void testGetAllTasksByUserId() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.findAllByUserId(1L)).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByUserId(1L);

        assertEquals(2, result.size());
        verify(taskRepository).findAllByUserId(1L);
    }

    @Test
    void testGetPendingTasksByUserId() {
        List<Task> tasks = Arrays.asList(new Task());
        when(taskRepository.findByUserIdAndCompletedFalseAndDeletedFalse(1L)).thenReturn(tasks);

        List<Task> result = taskService.getPendingTasksByUserId(1L);

        assertEquals(1, result.size());
        verify(taskRepository).findByUserIdAndCompletedFalseAndDeletedFalse(1L);
    }

    @Test
    void testDeleteTask_whenTaskExists() {
        Task task = new Task();
        task.setDeleted(false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        assertTrue(task.isDeleted());
        verify(taskRepository).save(task);
    }

    @Test
    void testDeleteTask_whenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        taskService.deleteTask(1L);

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testCreateTask_setsCreationDate() {
        Task task = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.createTask(task);

        assertNotNull(task.getCreationDate());
    }

    @Test
    void testCreateTask_callsRepositorySave() {
        Task task = new Task();
        taskService.createTask(task);

        verify(taskRepository).save(task);
    }
}
