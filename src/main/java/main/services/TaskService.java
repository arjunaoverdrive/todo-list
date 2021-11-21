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

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(User user) {
        List<Task> tasks = new ArrayList<>();
        try {
            tasks = taskRepository.findTasksByUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDeadline)
                        .thenComparing(Task::getName))
                .collect(Collectors.toList());
    }

    public Optional<Task> getTaskById(Integer id) {
        Optional<Task> task = null;
        try {
            task = taskRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    public Task saveTask(Task task, User user) {
        Task savedTask = null;
        try {
            task.setUser(user);
            logger.info("task " + task.getName() + " for user " + user + " has been sent to  backend");
            savedTask = taskRepository.save(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedTask;
    }


    public void deleteTask(Integer id) {
        try {
            taskRepository.deleteById(id);
            logger.info("task with id " + id + "has been removed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllTasks() {
        try {
            taskRepository.deleteAll();
            logger.info("all tasks have been removed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task updateTask(Task task, Task task2update, User user) {
        Task savedTask = null;
        try {
            task2update.setName(task.getName());
            task2update.setDescription(task.getDescription());
            task2update.setDeadline(task.getDeadline());
            logger.info("modified " + task2update);
            logger.info("put request has been sent to the repo");
            savedTask = saveTask(task2update, user);
        } catch (Exception e){
            e.printStackTrace();
        }
        return savedTask;
    }
}
