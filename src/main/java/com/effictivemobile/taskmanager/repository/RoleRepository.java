package com.effictivemobile.taskmanager.repository;


import com.effictivemobile.taskmanager.model.Role;
import com.effictivemobile.taskmanager.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByNameIn(Set<RoleName> names);
}
