package com.example.taskmanager.messaging;

import com.example.taskmanager.dto.NotificationMessage;
import com.example.taskmanager.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.taskmanager.model.Notification;


@Component
public class MessageListener {

    private final NotificationService notificationService;

    public MessageListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "#{T(com.example.taskmanager.config.RabbitMQConfig).QUEUE}")
    public void receiveMessage(NotificationMessage message) {
        notificationService.save(new Notification(
                null,
                message.getUserId(),
                message.getMessage(),
                false
        ));
    }




}
