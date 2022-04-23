package main.services;

import main.model.Task;
import main.model.User;
import main.repositories.TaskRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final Logger logger = Logger.getLogger(TaskService.class);
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    private User getCurrentUser(){
        return userService.getUserFromSecurityContext();
    }

    public List<Task> getTasks() {
        User user = getCurrentUser();
        List<Task> tasks = Optional.ofNullable(taskRepository.findTasksByUser(user))
                .orElseThrow(() -> new RuntimeException("Nothing is found for user " + user.toString()));

        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDeadline)
                        .thenComparing(Task::getName))
                .collect(Collectors.toList());
    }

    public Task getTaskById(Integer id) {
        Task task = Optional.ofNullable(taskRepository.findById(id)).get()
                .orElseThrow(() -> new NullPointerException("Task with id " + id + " doesn't exist"));
        return task;
    }

    public Task saveTask(Task task) {
        User user = getCurrentUser();
        Task savedTask = null;
        task.setUser(user);
        logger.info("task " + task.getName() + " for user " + user + " has been sent to backend");
        savedTask = taskRepository.save(task);
        return savedTask;
    }


    public void deleteTask(Integer id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
        logger.info("task with id " + id + " has been removed");

    }

    public void deleteAllTasks() {
        taskRepository.deleteAll();
        logger.info("all tasks have been removed");
    }

    public Task updateTask(Integer id, Task task) {
        Task taskFromDb = getTaskById(id);
        taskFromDb.setName(task.getName());
        taskFromDb.setDeadline(task.getDeadline());
        taskFromDb.setDescription(task.getDescription());
        logger.info("updated task " + taskFromDb.getId() + " for user " + getCurrentUser().toString());
        return saveTask(taskFromDb);
    }
}
