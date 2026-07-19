package com.stress.service;

import com.stress.dto.Dtos.*;
import com.stress.entity.User;
import com.stress.repository.PredictionRepository;
import com.stress.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import com.stress.entity.PredictionRecord;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PredictionRepository predictionRepository;

    public AdminService(UserRepository userRepository, PredictionRepository predictionRepository) {
        this.userRepository = userRepository;
        this.predictionRepository = predictionRepository;
    }

    public DashboardStats getSystemStats() {
        long totalUsers = userRepository.countUsers();
        long totalPredictions = predictionRepository.count();
        long lowCount = predictionRepository.countByStressLabel("Low");
        long medCount = predictionRepository.countByStressLabel("Medium");
        long highCount = predictionRepository.countByStressLabel("High");
        Double avgConf = predictionRepository.averageConfidence();
        long activeUsers = predictionRepository.countUsersWithPredictions();

        return DashboardStats.builder()
                .totalUsers(totalUsers)
                .totalPredictions(totalPredictions)
                .lowStressCount(lowCount)
                .mediumStressCount(medCount)
                .highStressCount(highCount)
                .averageConfidence(avgConf != null ? Math.round(avgConf * 100.0) / 100.0 : 0)
                .activeUsersWithPredictions(activeUsers)
                .build();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findByRole(User.Role.USER).stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToUserResponse(user);
    }

    public UserResponse updateUser(Long id, AdminUpdateUserRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (req.getActive() != null) user.setActive(req.getActive());
        if (req.getFullName() != null) user.setFullName(req.getFullName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getRole() != null) {
            try {
                user.setRole(User.Role.valueOf(req.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + req.getRole());
            }
        }

        userRepository.save(user);
        return mapToUserResponse(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Map<String, Object> getStressDistribution() {
        List<Object[]> distribution = predictionRepository.countByStressLevelGrouped();
        Map<String, Object> result = new LinkedHashMap<>();
        for (Object[] row : distribution) {
            result.put((String) row[0], row[1]);
        }
        return result;
    }

    public List<Map<String, Object>> getRecentPredictions(int limit) {
        return predictionRepository.findRecentPredictions(PageRequest.of(0, limit))
                .stream()
                .map(p -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", p.getId());
                    m.put("username", p.getUser().getUsername());
                    m.put("fullName", p.getUser().getFullName());
                    m.put("stressLabel", p.getStressLabel());
                    m.put("confidence", p.getConfidence());
                    m.put("createdAt", p.getCreatedAt());
                    return m;
                })
                .toList();
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
                .role(user.getRole().name())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .totalPredictions(predictions.size())
                .lastStressLabel(lastLabel)
                .build();
    }
}
