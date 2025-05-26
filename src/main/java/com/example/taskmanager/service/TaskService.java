package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    List<Task> getAllTasksByUserId(Long userId);
    List<Task> getPendingTasksByUserId(Long userId);
    void deleteTask(Long taskId);
}
