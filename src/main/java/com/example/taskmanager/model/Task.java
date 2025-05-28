package com.example.taskmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String description;

    private LocalDateTime creationDate;
    private LocalDateTime targetDate;

    private boolean completed;
    private boolean deleted;
}
