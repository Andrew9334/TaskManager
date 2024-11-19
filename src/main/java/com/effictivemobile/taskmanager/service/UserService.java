package com.effictivemobile.taskmanager.service;

import com.effictivemobile.taskmanager.exception.UserAlreadyExistsException;
import com.effictivemobile.taskmanager.exception.UserNotFoundException;
import com.effictivemobile.taskmanager.model.Role;
import com.effictivemobile.taskmanager.model.RoleName;
import com.effictivemobile.taskmanager.model.User;
import com.effictivemobile.taskmanager.repository.RoleRepository;
import com.effictivemobile.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password, Set<RoleName> roleNames) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);

        Set<Role> roles = getRolesFromNames(roleNames);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword);
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }


    private Set<Role> getRolesFromNames(Set<RoleName> roleNames) {
        Set<Role> roles = roleRepository.findByNameIn(roleNames);
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Invalid roles specified");
        }
        return roles;
    }
}
