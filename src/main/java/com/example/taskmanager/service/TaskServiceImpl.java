package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        task.setCreationDate(LocalDateTime.now());
        task.setCompleted(false);
        task.setDeleted(false);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasksByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    public List<Task> getPendingTasksByUserId(Long userId) {
        return taskRepository.findByUserIdAndCompletedFalseAndDeletedFalse(userId);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            task.setDeleted(true);
            taskRepository.save(task);
        });
    }
}
