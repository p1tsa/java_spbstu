package com.example.taskmanager.repository.inmemory;

import com.example.taskmanager.model.Notification;
import com.example.taskmanager.repository.NotificationRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Profile("inmemory")
public class InMemoryNotificationRepository implements NotificationRepository {

    private final Map<Long, Notification> notifications = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(idGenerator.getAndIncrement());
        }
        notifications.put(notification.getId(), notification);
        return notification;
    }

    @Override
    public List<Notification> findAllByUserId(Long userId) {
        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findUnreadByUserId(Long userId) {
        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .collect(Collectors.toList());
    }
}
