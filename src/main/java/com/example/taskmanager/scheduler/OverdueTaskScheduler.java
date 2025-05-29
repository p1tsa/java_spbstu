package com.example.taskmanager.scheduler;

import com.example.taskmanager.dto.NotificationMessage;
import com.example.taskmanager.messaging.MessagePublisher;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OverdueTaskScheduler {

    private final TaskRepository taskRepository;
    private final MessagePublisher messagePublisher;

    public OverdueTaskScheduler(TaskRepository taskRepository, MessagePublisher messagePublisher) {
        this.taskRepository = taskRepository;
        this.messagePublisher = messagePublisher;
    }

    @Scheduled(fixedRate = 60000)
    public void checkOverdueTasks() {
        System.out.println("Scheduled check: looking for overdue tasks...");

        List<Task> overdueTasks = taskRepository.findByTargetDateBeforeAndCompletedFalse(LocalDateTime.now());

        if (overdueTasks.isEmpty()) {
            System.out.println("No overdue tasks found.");
        } else {
            System.out.println("Found " + overdueTasks.size() + " overdue task(s). Sending notifications...");

            for (Task task : overdueTasks) {
                NotificationMessage message = new NotificationMessage(
                        task.getUserId(),
                        "Task overdue: " + task.getDescription()
                );
                messagePublisher.sendNotification(message);

                System.out.println("Notification sent for task: " + task.getDescription());
            }
        }
    }

}
