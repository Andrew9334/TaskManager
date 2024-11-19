package com.effictivemobile.taskmanager.controller;

import com.effictivemobile.taskmanager.model.Task;
import com.effictivemobile.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    // Тестирование пагинации
    @Test
    public void testGetAllTasksWithPagination() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);
        Page<Task> taskPage = new PageImpl<>(tasks, PageRequest.of(0, 10), tasks.size());

        when(taskService.getAllTasks(PageRequest.of(0, 10))).thenReturn(taskPage);

        mockMvc.perform(get("/tasks?page=0&size=10"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.content.length()").value(2))  // Проверка длины содержимого страницы
                .andExpect(jsonPath("$.content[0].title").value("Task 1"))
                .andExpect(jsonPath("$.content[1].title").value("Task 2"));
    }
}
