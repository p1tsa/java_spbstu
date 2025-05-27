package com.example.taskmanager.service;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        User input = User.builder().username("alice").build();
        User saved = User.builder().id(1L).username("alice").build();

        when(userRepository.save(input)).thenReturn(saved);

        User result = userService.register(input);

        assertEquals(1L, result.getId());
        assertEquals("alice", result.getUsername());
        verify(userRepository).save(input);
    }

    @Test
    void testLoginFound() {
        User user = User.builder().id(1L).username("alice").build();

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        Optional<User> result = userService.login("alice");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testLoginNotFound() {
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());

        Optional<User> result = userService.login("bob");

        assertFalse(result.isPresent());
    }

    @Test
    void testRegisterWithNullUsernameThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            User.builder().username(null).build();
        });
    }

    @Test
    void testRegisterDuplicateUsername() {
        User user1 = User.builder().username("alice").build();
        User user2 = User.builder().username("alice").build();

        when(userRepository.save(user1)).thenReturn(User.builder().id(1L).username("alice").build());
        when(userRepository.save(user2)).thenReturn(User.builder().id(2L).username("alice").build());

        User created1 = userService.register(user1);
        User created2 = userService.register(user2);

        assertEquals("alice", created1.getUsername());
        assertEquals("alice", created2.getUsername());

        verify(userRepository, times(2)).save(any(User.class));
    }
}
