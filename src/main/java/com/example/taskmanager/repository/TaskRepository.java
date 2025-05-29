package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(Long userId);
    List<Task> findByUserIdAndCompletedFalseAndDeletedFalse(Long userId);

    List<Task> findByTargetDateBeforeAndCompletedFalse(LocalDateTime dateTime);
}
