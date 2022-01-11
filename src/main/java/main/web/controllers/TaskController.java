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
public class TaskController {

    private final Logger logger = Logger.getLogger(TaskController.class);

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @ModelAttribute("tasks")
    public List<Task> tasks() {
        User user = userService.getUserFromSecurityContext();
        return taskService.getTasks(user);
    }

    @GetMapping(value = "/tasks/")
    public ResponseEntity<List<Task>> list() {
        User user = userService.getUserFromSecurityContext();
        logger.info("getting tasks for user " + user);
        List<Task> taskList = taskService.getTasks(user);
        logger.info("found " + taskList.size() + " tasks");
        if (taskList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(taskList);
    }

    @GetMapping(value = "/tasks/{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (!taskOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Task task = (Task) taskOptional.get();
        model.addAttribute("task", task);
        logger.info("task " + task + " has been retrieved from the repo");
        return ResponseEntity.ok(task);
    }

    @PostMapping(value = "/tasks/")
    public ResponseEntity<Integer> add(Task task, Model model) {
        model.addAttribute("task2add", task);
        User user = userService.getUserFromSecurityContext();
        taskService.saveTask(task, user);
        logger.info("saving task for user " + user);
        return ResponseEntity.ok(task.getId());
    }


    @PutMapping(value = "/tasks/{id}")
    public ResponseEntity<Task> update(@PathVariable int id, Task task) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (!taskOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Task task2update = taskOptional.get();
        logger.info("retrieved " + task2update);
        User user = userService.getUserFromSecurityContext();
        logger.info("updating task for user " + user);
        return ResponseEntity.ok(taskService.updateTask(task, task2update, user));
    }

    @DeleteMapping(value = "/tasks/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (!taskOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        taskService.deleteTask(id);
        logger.info("deleting task " + taskOptional);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping(value = "/tasks/")
    public ResponseEntity<HttpStatus> deleteAll() {
        logger.info("deleting tasks");
        taskService.deleteAllTasks();
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
