package com.example.taskmanager.service;

import com.example.taskmanager.model.Notification;
import com.example.taskmanager.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    private NotificationRepository notificationRepository;
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        notificationService = new NotificationServiceImpl(notificationRepository);
    }

    @Test
    void testSaveNotification() {
        Notification notification = Notification.builder()
                .userId(1L)
                .message("New task")
                .read(false)
                .build();

        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.save(notification);

        assertNotNull(result);
        assertEquals("New task", result.getMessage());
        verify(notificationRepository).save(notification);
    }

    @Test
    void testGetAllByUserId_returnsList() {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepository.findAllByUserId(1L)).thenReturn(notifications);

        List<Notification> result = notificationService.getAllByUserId(1L);

        assertEquals(2, result.size());
        verify(notificationRepository).findAllByUserId(1L);
    }

    @Test
    void testGetAllByUserId_emptyList() {
        when(notificationRepository.findAllByUserId(999L)).thenReturn(List.of());

        List<Notification> result = notificationService.getAllByUserId(999L);

        assertTrue(result.isEmpty());
        verify(notificationRepository).findAllByUserId(999L);
    }

    @Test
    void testGetUnreadByUserId_returnsList() {
        List<Notification> notifications = List.of(
                Notification.builder().userId(1L).message("Unread").read(false).build()
        );

        when(notificationRepository.findByUserIdAndReadFalse(1L)).thenReturn(notifications);

        List<Notification> result = notificationService.getUnreadByUserId(1L);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isRead());
        verify(notificationRepository).findByUserIdAndReadFalse(1L);
    }

    @Test
    void testGetUnreadByUserId_emptyList() {
        when(notificationRepository.findByUserIdAndReadFalse(2L)).thenReturn(List.of());

        List<Notification> result = notificationService.getUnreadByUserId(2L);

        assertTrue(result.isEmpty());
        verify(notificationRepository).findByUserIdAndReadFalse(2L);
    }

    @Test
    void testSaveNotification_setsReadToFalse() {
        Notification notification = Notification.builder()
                .userId(1L)
                .message("Initial")
                .read(false)
                .build();

        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification saved = notificationService.save(notification);

        assertFalse(saved.isRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void testSaveNotification_preservesUserId() {
        Notification notification = Notification.builder()
                .userId(77L)
                .message("Info")
                .read(false)
                .build();

        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.save(notification);

        assertEquals(77L, result.getUserId());
        verify(notificationRepository).save(notification);
    }
}
