package com.example.taskmanager.service;

import com.example.taskmanager.dto.NotificationMessage;
import com.example.taskmanager.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncNotificationService {

    private final NotificationService notificationService;

    @Async
    public void saveNotificationAsync(NotificationMessage message) {
        Notification notification = new Notification(
                null,
                message.getUserId(),
                message.getMessage(),
                false
        );
        notificationService.save(notification);
        log.info("Saved notification asynchronously: {}", message);
    }
}
