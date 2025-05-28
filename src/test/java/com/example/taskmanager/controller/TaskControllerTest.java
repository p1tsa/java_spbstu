package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTask() throws Exception {
        Task input = Task.builder()
                .userId(1L)
                .description("Test task")
                .targetDate(LocalDateTime.now())
                .build();

        Task saved = input.toBuilder().id(10L).creationDate(LocalDateTime.now()).build();

        Mockito.when(taskService.createTask(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.description").value("Test task"));
    }

    @Test
    void testGetAllTasks() throws Exception {
        List<Task> tasks = List.of(
                new Task(1L, 1L, "Task 1", LocalDateTime.now(), LocalDateTime.now(), false, false),
                new Task(2L, 1L, "Task 2", LocalDateTime.now(), LocalDateTime.now(), true, false)
        );

        Mockito.when(taskService.getAllTasksByUserId(1L)).thenReturn(tasks);

        mockMvc.perform(get("/tasks/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetPendingTasks() throws Exception {
        List<Task> pendingTasks = List.of(
                new Task(3L, 1L, "Pending task", LocalDateTime.now(), LocalDateTime.now(), false, false)
        );

        Mockito.when(taskService.getPendingTasksByUserId(1L)).thenReturn(pendingTasks);

        mockMvc.perform(get("/tasks/user/1/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Pending task"));
    }

    @Test
    void testDeleteTask() throws Exception {
        Mockito.doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(taskService).deleteTask(1L);
    }
}
