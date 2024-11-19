package com.effictivemobile.taskmanager.service;

import com.effictivemobile.taskmanager.exception.UserAlreadyExistsException;
import com.effictivemobile.taskmanager.exception.UserNotFoundException;
import com.effictivemobile.taskmanager.model.Role;
import com.effictivemobile.taskmanager.model.RoleName;
import com.effictivemobile.taskmanager.model.User;
import com.effictivemobile.taskmanager.repository.RoleRepository;
import com.effictivemobile.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private final String email = "test@example.com";
    private final String password = "password";
    private final Set<RoleName> roles = Set.of(RoleName.USER);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(email, password, roles));
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Role role = new Role();
        role.setName(RoleName.USER);
        when(roleRepository.findByNameIn(roles)).thenReturn(Set.of(role));

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        User newUser = userService.registerUser(email, password, roles);

        assertNotNull(newUser);
        assertEquals(email, newUser.getEmail());
        assertEquals("encodedPassword", newUser.getPassword());
        assertEquals(roles, newUser.getRoles());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    void testGetUserByEmail_Success() {
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        assertEquals("encodedPassword", foundUser.getPassword());
    }
}
