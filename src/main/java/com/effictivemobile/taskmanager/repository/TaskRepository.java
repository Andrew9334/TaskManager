package com.effictivemobile.taskmanager.repository;


import com.effictivemobile.taskmanager.model.Task;
import com.effictivemobile.taskmanager.model.TaskPriority;
import com.effictivemobile.taskmanager.model.TaskStatus;
import com.effictivemobile.taskmanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    List<Task> findByAuthor(User author);
    List<Task> findByAssignee(User assignee);
    List<Task> findByAssigneeIn(Set<User> assignees);
}
