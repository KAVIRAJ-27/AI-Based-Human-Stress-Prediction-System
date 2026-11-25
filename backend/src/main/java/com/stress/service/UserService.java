package com.stress.service;

import com.stress.dto.Dtos.*;
import com.stress.entity.PredictionRecord;
import com.stress.entity.User;
import com.stress.repository.PredictionRepository;
import com.stress.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PredictionRepository predictionRepository;

    public UserService(UserRepository userRepository, PredictionRepository predictionRepository) {
        this.userRepository = userRepository;
        this.predictionRepository = predictionRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username));
        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(String username, UpdateUserRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username));

        // Validate and update email
        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            String newEmail = req.getEmail().trim().toLowerCase();
            if (!newEmail.equalsIgnoreCase(user.getEmail())) {
                userRepository.findByEmail(newEmail).ifPresent(existing -> {
                    if (!existing.getId().equals(user.getId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered to another account");
                    }
                });
                user.setEmail(newEmail);
            }
        }

        if (req.getFullName() != null) {
            user.setFullName(req.getFullName().trim());
        }

        if (req.getPhone() != null) {
            user.setPhone(req.getPhone().trim());
        }

        if (req.getAge() != null) {
            if (req.getAge() < 1 || req.getAge() > 130) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age must be between 1 and 130");
            }
            user.setAge(req.getAge());
        }

        if (req.getGender() != null) {
            user.setGender(req.getGender().trim());
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    private UserResponse mapToUserResponse(User user) {
        List<PredictionRecord> predictions = predictionRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        String lastLabel = predictions.isEmpty() ? "N/A" : predictions.get(0).getStressLabel();

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .age(user.getAge())
                .gender(user.getGender())
                .phone(user.getPhone())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .totalPredictions(predictions.size())
                .lastStressLabel(lastLabel)
                .build();
    }
}
