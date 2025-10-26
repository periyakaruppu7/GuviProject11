package com.example.quizapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.quizapp.entity.User;
import com.example.quizapp.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1L);
        user1.setUsername("john");
        user1.setPassword("1234");
        user1.setRole(null); // test default role assignment

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane");
        user2.setPassword("abcd");
        user2.setRole("ADMIN");
    }

    @Test
    void testSaveUser_DefaultRole() {
        userService.saveUser(user1);
        assertEquals("USER", user1.getRole()); // default role assigned
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testValidateUser_Success() {
        when(userRepository.findByUsername("john")).thenReturn(user1);

        boolean result = userService.validateUser("john", "1234");
        assertTrue(result);
    }

    @Test
    void testValidateUser_Failure_WrongPassword() {
        when(userRepository.findByUsername("john")).thenReturn(user1);

        boolean result = userService.validateUser("john", "wrong");
        assertFalse(result);
    }

    @Test
    void testValidateUser_Failure_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        boolean result = userService.validateUser("unknown", "1234");
        assertFalse(result);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User user = userService.getUserById(1L);
        assertEquals(user1, user);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User user = userService.getUserById(99L);
        assertNull(user);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("jane")).thenReturn(user2);

        User user = userService.findByUsername("jane");
        assertEquals(user2, user);
    }
}
