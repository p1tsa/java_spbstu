package com.example.taskmanager.service;

import com.example.taskmanager.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification save(Notification notification);
    List<Notification> getAllByUserId(Long userId);
    List<Notification> getUnreadByUserId(Long userId);
}
