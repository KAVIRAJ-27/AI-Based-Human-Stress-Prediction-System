package com.stress.controller;

import com.stress.dto.Dtos.*;
import com.stress.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        return ResponseEntity.ok(adminService.getSystemStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody AdminUpdateUserRequest req) {
        return ResponseEntity.ok(adminService.updateUser(id, req));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @GetMapping("/stress-distribution")
    public ResponseEntity<Map<String, Object>> getStressDistribution() {
        return ResponseEntity.ok(adminService.getStressDistribution());
    }

    @GetMapping("/recent-predictions")
    public ResponseEntity<List<Map<String, Object>>> getRecentPredictions(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(adminService.getRecentPredictions(limit));
    }
}
