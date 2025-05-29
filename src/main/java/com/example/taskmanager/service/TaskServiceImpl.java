package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.example.taskmanager.messaging.MessagePublisher;
import com.example.taskmanager.dto.NotificationMessage;



import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CacheManager cacheManager;
    private final MessagePublisher messagePublisher;


    public TaskServiceImpl(TaskRepository taskRepository, CacheManager cacheManager, MessagePublisher messagePublisher) {
        this.taskRepository = taskRepository;
        this.cacheManager = cacheManager;
        this.messagePublisher = messagePublisher;
    }


    @Override
    @CacheEvict(value = {"tasks", "pendingTasks"}, key = "#task.userId")
    public Task createTask(Task task) {
        task.setCreationDate(LocalDateTime.now());
        task.setCompleted(false);
        task.setDeleted(false);
        Task saved = taskRepository.save(task);

        NotificationMessage message = new NotificationMessage(
                saved.getUserId(),
                "New task created: " + saved.getDescription()
        );

        System.out.println(">>> Sending message to RabbitMQ: " + message);

        messagePublisher.sendNotification(message);

        return saved;
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
