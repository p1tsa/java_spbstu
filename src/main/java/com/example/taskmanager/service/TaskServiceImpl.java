package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CacheManager cacheManager;

    public TaskServiceImpl(TaskRepository taskRepository, CacheManager cacheManager) {
        this.taskRepository = taskRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @CacheEvict(value = {"tasks", "pendingTasks"}, key = "#task.userId")
    public Task createTask(Task task) {
        task.setCreationDate(LocalDateTime.now());
        task.setCompleted(false);
        task.setDeleted(false);
        return taskRepository.save(task);
    }

    @Override
    @Cacheable(value = "tasks", key = "#userId")
    public List<Task> getAllTasksByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Cacheable(value = "pendingTasks", key = "#userId")
    public List<Task> getPendingTasksByUserId(Long userId) {
        return taskRepository.findByUserIdAndCompletedFalseAndDeletedFalse(userId);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            task.setDeleted(true);
            taskRepository.save(task);

            Long userId = task.getUserId();
            cacheManager.getCache("tasks").evict(userId);
            cacheManager.getCache("pendingTasks").evict(userId);
        });
    }
}
