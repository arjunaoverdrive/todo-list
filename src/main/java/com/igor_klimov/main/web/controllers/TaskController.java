package com.igor_klimov.main.web.controllers;

import com.igor_klimov.main.model.Task;
import com.igor_klimov.main.services.TaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final Logger logger = Logger.getLogger(TaskController.class);

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") int id) {
        Task task = taskService.getTaskById(id);
        logger.info("task " + task + " has been retrieved from the repo");
        return ResponseEntity.ok(task);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Integer> addTask(Task task) {
        taskService.saveTask(task);
        return ResponseEntity.ok(task.getId());
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") int id, Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<HttpStatus> deleteAll() {
        logger.info("deleting tasks");
        taskService.deleteAllTasks();
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
