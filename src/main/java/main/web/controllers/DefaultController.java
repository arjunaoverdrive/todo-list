package main.web.controllers;

import main.model.User;
import main.services.TaskService;
import main.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import main.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class DefaultController {

    private final Logger logger = Logger.getLogger(DefaultController.class);
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public DefaultController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @ModelAttribute("tasks")
    public List<Task> tasks() {
        return taskService.getTasks();
    }

    @ModelAttribute("task2add")
    public Task task() {
        return new Task();
    }

    @ModelAttribute("date")
    public String date() {
        TimeZone tz = TimeZone.getTimeZone("Europe/Moscow");
        Calendar calendar = Calendar.getInstance(tz);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        calendar.add(Calendar.HOUR, 1);
        calendar.set(Calendar.MINUTE, 0);
        String proposedString = format.format(calendar.getTime());
        return proposedString;
    }

    @ModelAttribute("user")
    public User user() {
        User user = userService.getUserFromSecurityContext();
        return user;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}