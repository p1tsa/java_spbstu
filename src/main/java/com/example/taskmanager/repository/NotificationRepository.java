package com.example.taskmanager.repository;

import com.example.taskmanager.model.Notification;

import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> findAllByUserId(Long userId);
    List<Notification> findUnreadByUserId(Long userId);
}
