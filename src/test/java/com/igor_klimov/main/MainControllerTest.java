package com.igor_klimov.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igor_klimov.main.model.Task;
import com.igor_klimov.main.repositories.UserRepository;
import com.igor_klimov.main.web.controllers.TaskController;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = "igor1984s@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/tasks-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/tasks-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TaskController controller;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void mainPageTest() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='central-container']/div[2]/span").string("Igor"));
    }

    @Test
    public void tasksList() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='task-list']/div").nodeCount(2))
                .andExpect(xpath("//*[@id='task-list']/div[@data-id=1]").exists());
    }

    @Test
    public void addTaskToListTest() throws Exception {
        Task task = new Task();
        task.setName("new task");
        task.setDeadline(LocalDateTime.now().plusDays(5));
        task.setDescription("todo");
        task.setUser(userRepository.findById(1).get());
        MvcResult result = this.mockMvc.perform(post("/tasks/")
                        .content(String.valueOf(task)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void updateTaskTest() throws Exception {
        MvcResult result = this.mockMvc.perform(put("/tasks/1")
                        .content("'name' : 'ToDoList', 'deadline': '2022-05-15 18:00:00', 'description': 'todo', 'user_id' : '1'"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertThat(result).isNotNull();

        Assertions.assertThat(result.getResponse().getContentAsString().startsWith("{'id':'1'"));
    }

    @Test
    public void getTaskTest() throws Exception{
        MvcResult result = this.mockMvc.perform(get("/tasks/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getResponse().getContentAsString().contains(
                "'Переписать на микросервисах. Сделать сервис для юзеров, сервис для задач'"));
    }

    @Test
    public void deleteTaskTest() throws Exception{
        MvcResult result = this.mockMvc.perform(delete("/tasks/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertThat(result).isNotNull();
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='task-list']/div").nodeCount(1));
    }

    @Test
    public void deleteAllTasksTest() throws Exception{
        MvcResult result = this.mockMvc.perform(delete("/tasks/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertThat(result).isNotNull();
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='task-list']/div").nodeCount(0));
    }
}
