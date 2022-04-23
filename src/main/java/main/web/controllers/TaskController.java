package main.web.controllers;

import main.model.Task;
import main.model.User;
import main.services.TaskService;
import main.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
