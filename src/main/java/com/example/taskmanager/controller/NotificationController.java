package com.example.taskmanager.controller;

import com.example.taskmanager.model.Notification;
import com.example.taskmanager.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getAll(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getAllByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnread(@PathVariable Long userId) {
        List<Notification> unread = notificationService.getUnreadByUserId(userId);
        return ResponseEntity.ok(unread);
    }
}
