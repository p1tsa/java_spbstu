package com.example.taskmanager.service;

import com.example.taskmanager.model.Notification;
import com.example.taskmanager.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNotification() {
        Notification input = new Notification(null, 1L, "Test message", false);
        Notification saved = new Notification(1L, 1L, "Test message", false);

        when(notificationRepository.save(input)).thenReturn(saved);

        Notification result = notificationService.save(input);

        assertEquals(1L, result.getId());
        assertEquals("Test message", result.getMessage());
        assertFalse(result.isRead());
        verify(notificationRepository).save(input);
    }

    @Test
    void testGetAllByUserId() {
        List<Notification> notifications = List.of(
                new Notification(1L, 1L, "Message 1", false),
                new Notification(2L, 1L, "Message 2", true)
        );

        when(notificationRepository.findAllByUserId(1L)).thenReturn(notifications);

        List<Notification> result = notificationService.getAllByUserId(1L);

        assertEquals(2, result.size());
        verify(notificationRepository).findAllByUserId(1L);
    }

    @Test
    void testGetUnreadByUserId() {
        List<Notification> unread = List.of(
                new Notification(3L, 1L, "Unread message", false)
        );

        when(notificationRepository.findUnreadByUserId(1L)).thenReturn(unread);

        List<Notification> result = notificationService.getUnreadByUserId(1L);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isRead());
        verify(notificationRepository).findUnreadByUserId(1L);
    }

    @Test
    void testSaveReadNotification() {
        Notification input = new Notification(null, 2L, "Read message", true);
        Notification saved = new Notification(5L, 2L, "Read message", true);

        when(notificationRepository.save(input)).thenReturn(saved);

        Notification result = notificationService.save(input);

        assertTrue(result.isRead());
        assertEquals("Read message", result.getMessage());
        verify(notificationRepository).save(input);
    }

    @Test
    void testGetAllByUserIdEmpty() {
        when(notificationRepository.findAllByUserId(999L)).thenReturn(List.of());

        List<Notification> result = notificationService.getAllByUserId(999L);

        assertTrue(result.isEmpty());
        verify(notificationRepository).findAllByUserId(999L);
    }

    @Test
    void testSaveNotificationWithNullMessage() {
        Notification input = new Notification(null, 1L, null, false);
        Notification saved = new Notification(10L, 1L, null, false);

        when(notificationRepository.save(input)).thenReturn(saved);

        Notification result = notificationService.save(input);

        assertNull(result.getMessage());
        verify(notificationRepository).save(input);
    }
}
