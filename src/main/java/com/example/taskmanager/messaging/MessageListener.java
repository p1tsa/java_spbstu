package com.example.taskmanager.messaging;

import com.example.taskmanager.dto.NotificationMessage;
import com.example.taskmanager.service.AsyncNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener {

    private final AsyncNotificationService asyncNotificationService;

    @RabbitListener(queues = "#{T(com.example.taskmanager.config.RabbitMQConfig).QUEUE}")
    public void receiveMessage(NotificationMessage message) {
        asyncNotificationService.saveNotificationAsync(message);
    }
}
