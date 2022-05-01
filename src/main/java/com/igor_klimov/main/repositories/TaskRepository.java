package com.igor_klimov.main.repositories;

import com.igor_klimov.main.model.Task;
import com.igor_klimov.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task>findTasksByUser(User user);
}
