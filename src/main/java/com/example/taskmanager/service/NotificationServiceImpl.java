package com.example.taskmanager.service;

import com.example.taskmanager.model.Notification;
import com.example.taskmanager.repository.NotificationRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("inmemory")
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllByUserId(Long userId) {
        return notificationRepository.findAllByUserId(userId);
    }

    @Override
    public List<Notification> getUnreadByUserId(Long userId) {
        return notificationRepository.findUnreadByUserId(userId);
    }
}
