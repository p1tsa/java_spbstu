package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testRegisterUser() throws Exception {
        User input = User.builder().username("alice").build();
        User saved = User.builder().id(1L).username("alice").build();

        Mockito.when(userService.register(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = User.builder().id(1L).username("alice").build();
        Mockito.when(userService.login("alice")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/login")
                        .param("username", "alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        Mockito.when(userService.login("bob")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/login")
                        .param("username", "bob"))
                .andExpect(status().isNotFound());
    }
}
