package com.stress.service;

import com.stress.dto.Dtos.*;
import com.stress.entity.User;
import com.stress.repository.PredictionRepository;
import com.stress.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PredictionRepository predictionRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .fullName("John Doe")
                .age(28)
                .gender("Male")
                .phone("1234567890")
                .role(User.Role.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getProfile_whenUserExists_returnsUserResponse() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(sampleUser));
        when(predictionRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(Collections.emptyList());

        UserResponse response = userService.getProfile("johndoe");

        assertNotNull(response);
        assertEquals("johndoe", response.getUsername());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("John Doe", response.getFullName());
        assertEquals(28, response.getAge());
        assertEquals("Male", response.getGender());
        assertEquals("1234567890", response.getPhone());
        assertEquals("USER", response.getRole());
    }

    @Test
    void getProfile_whenUserNotFound_throwsNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.getProfile("nonexistent")
        );
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void updateProfile_whenValidInputs_updatesAndReturnsUserResponse() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(predictionRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(Collections.emptyList());

        UpdateUserRequest req = new UpdateUserRequest("Johnathan Doe", 29, "Male", "9876543210", "johnathan@example.com");
        when(userRepository.findByEmail("johnathan@example.com")).thenReturn(Optional.empty());

        UserResponse updated = userService.updateProfile("johndoe", req);

        assertEquals("Johnathan Doe", updated.getFullName());
        assertEquals("johnathan@example.com", updated.getEmail());
        assertEquals(29, updated.getAge());
        assertEquals("9876543210", updated.getPhone());
        assertEquals("USER", updated.getRole()); // Role untouched
        verify(userRepository).save(sampleUser);
    }

    @Test
    void updateProfile_whenEmailAlreadyTakenByAnotherUser_throwsBadRequest() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(sampleUser));

        User otherUser = User.builder().id(2L).username("janedoe").email("jane@example.com").build();
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(otherUser));

        UpdateUserRequest req = new UpdateUserRequest("John Doe", 28, "Male", "1234567890", "jane@example.com");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.updateProfile("johndoe", req)
        );
        assertEquals(400, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("already registered"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateProfile_whenInvalidAge_throwsBadRequest() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(sampleUser));

        UpdateUserRequest req = new UpdateUserRequest("John Doe", 150, "Male", "1234567890", "john@example.com");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.updateProfile("johndoe", req)
        );
        assertEquals(400, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Age must be between 1 and 130"));
    }
}
