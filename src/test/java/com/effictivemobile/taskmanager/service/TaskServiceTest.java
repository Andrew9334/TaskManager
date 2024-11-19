package com.effictivemobile.taskmanager.service;

import com.effictivemobile.taskmanager.exception.TaskNotFoundException;
import com.effictivemobile.taskmanager.model.Task;
import com.effictivemobile.taskmanager.model.TaskPriority;
import com.effictivemobile.taskmanager.model.TaskStatus;
import com.effictivemobile.taskmanager.model.User;
import com.effictivemobile.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private User mockUser;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("This is a test task description");
        task.setStatus(TaskStatus.NEW);
        task.setPriority(TaskPriority.HIGH);
        task.setAuthor(mockUser);
        task.setAssignee(mockUser);
        task.setComments(List.of());

        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void testGetAllTasks() {
        Page<Task> mockPage = mock(Page.class);
        when(taskRepository.findAll(pageable)).thenReturn(mockPage);

        Page<Task> tasks = taskService.getAllTasks(pageable);

        verify(taskRepository, times(1)).findAll(pageable);

        assertNotNull(tasks);
    }

    @Test
    public void testCreateTask() {
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        verify(taskRepository, times(1)).save(task);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("This is a test task description", createdTask.getDescription());
        assertEquals(TaskStatus.NEW, createdTask.getStatus());
        assertEquals(TaskPriority.HIGH, createdTask.getPriority());
    }

    @Test
    public void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated task description");
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);
        updatedTask.setPriority(TaskPriority.MEDIUM);

        Task result = taskService.updateTask(1L, updatedTask);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(updatedTask);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated task description", result.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());
    }

    @Test
    public void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testTaskNotFoundExceptionOnUpdate() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Non-existing Task");
        updatedTask.setDescription("This task does not exist");
        updatedTask.setStatus(TaskStatus.NEW);
        updatedTask.setPriority(TaskPriority.LOW);

        TaskNotFoundException thrown = assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(1L, updatedTask);
        });

        assertEquals("Task not found", thrown.getMessage());
    }

    @Test
    public void testCreateTaskWithNullTitle() {
        task.setTitle(null);

        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(task));
    }
}
