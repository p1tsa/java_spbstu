package com.example.taskmanager.repository.inmemory;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Profile("inmemory")
public class InMemoryTaskRepository implements TaskRepository {

    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator.getAndIncrement());
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        Task task = tasks.get(id);
        return (task != null && !task.isDeleted()) ? Optional.of(task) : Optional.empty();
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        return tasks.values().stream()
                .filter(t -> t.getUserId().equals(userId) && !t.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findPendingByUserId(Long userId) {
        return tasks.values().stream()
                .filter(t -> t.getUserId().equals(userId) && !t.isCompleted() && !t.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public void softDelete(Long id) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setDeleted(true);
        }
    }
}
