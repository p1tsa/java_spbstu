package com.example.taskmanager.service;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testRegisterUser_savesUser() {
        User user = User.builder().username("alice").build();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.register(user);

        assertNotNull(result);
        assertEquals("alice", result.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void testRegisterUser_returnsSavedUser() {
        User user = User.builder().username("bob").build();
        User savedUser = User.builder().id(1L).username("bob").build();
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.register(user);

        assertEquals(1L, result.getId());
        assertEquals("bob", result.getUsername());
    }

    @Test
    void testLogin_existingUser_returnsUser() {
        User user = User.builder().id(2L).username("john").build();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        Optional<User> result = userService.login("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        verify(userRepository).findByUsername("john");
    }

    @Test
    void testLogin_nonExistingUser_returnsEmptyOptional() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> result = userService.login("nonexistent");

        assertFalse(result.isPresent());
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void testRegister_nullUsername() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.register(user);

        assertNull(result.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void testLogin_callsRepositoryExactlyOnce() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        userService.login("admin");

        verify(userRepository, times(1)).findByUsername("admin");
    }

    @Test
    void testRegister_callsSaveOnce() {
        User user = User.builder().username("test").build();
        userService.register(user);

        verify(userRepository, times(1)).save(user);
    }
}
