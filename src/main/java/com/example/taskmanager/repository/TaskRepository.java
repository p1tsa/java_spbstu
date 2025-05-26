package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAllByUserId(Long userId);
    List<Task> findPendingByUserId(Long userId);
    void softDelete(Long id);
}
