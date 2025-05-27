package com.example.taskmanager.controller;

import com.example.taskmanager.model.Notification;
import com.example.taskmanager.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testGetAllNotifications() throws Exception {
        List<Notification> notifications = List.of(
                new Notification(1L, 1L, "Hello", false),
                new Notification(2L, 1L, "World", true)
        );

        when(notificationService.getAllByUserId(1L)).thenReturn(notifications);

        mockMvc.perform(get("/notifications/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].message").value("Hello"))
                .andExpect(jsonPath("$[1].message").value("World"));
    }

    @Test
    void testGetUnreadNotifications() throws Exception {
        List<Notification> unread = List.of(
                new Notification(3L, 1L, "New message", false)
        );

        when(notificationService.getUnreadByUserId(1L)).thenReturn(unread);

        mockMvc.perform(get("/notifications/user/1/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].message").value("New message"))
                .andExpect(jsonPath("$[0].read").value(false));
    }
}
