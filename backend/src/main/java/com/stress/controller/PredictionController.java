package com.stress.controller;

import com.stress.dto.Dtos.*;
import com.stress.service.PredictionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/predict")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping
    public ResponseEntity<PredictionResponse> predict(
            @Valid @RequestBody PredictionRequest req,
            Authentication auth) {
        return ResponseEntity.ok(predictionService.predict(req, auth.getName()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<PredictionHistoryItem>> getHistory(Authentication auth) {
        return ResponseEntity.ok(predictionService.getUserHistory(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PredictionResponse> getById(
            @PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(predictionService.getPredictionById(id, auth.getName()));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<UserDashboard> getDashboard(Authentication auth) {
        return ResponseEntity.ok(predictionService.getUserDashboard(auth.getName()));
    }
}
