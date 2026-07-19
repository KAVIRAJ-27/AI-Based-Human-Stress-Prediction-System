package com.stress.service;

import com.stress.dto.Dtos.*;
import com.stress.entity.PredictionRecord;
import com.stress.entity.User;
import com.stress.repository.PredictionRepository;
import com.stress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public PredictionService(PredictionRepository predictionRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.predictionRepository = predictionRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public PredictionResponse predict(PredictionRequest req, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Build ML input
        Map<String, Object> mlInput = buildMlInput(req);

        // Call Python ML service
        Map<String, Object> mlResponse;
        try {
            mlResponse = restTemplate.postForObject(
                    mlServiceUrl + "/predict", mlInput, Map.class);
        } catch (Exception e) {
            throw new IllegalStateException("ML service unavailable: " + e.getMessage(), e);
        }

        // Parse response
        Integer stressLevel = (Integer) mlResponse.get("stress_level");
        String stressLabel = (String) mlResponse.get("stress_label");
        Double confidence = ((Number) mlResponse.get("confidence")).doubleValue();
        Map<String, Object> probMap = (Map<String, Object>) mlResponse.get("probabilities");
        List<String> recommendations = (List<String>) mlResponse.get("recommendations");
        Map<String, Object> topFactors = (Map<String, Object>) mlResponse.get("top_contributing_factors");

        // Save to DB
        PredictionRecord predictionRecord = PredictionRecord.builder()
                .user(user)
                .anxietyLevel(req.getAnxietyLevel())
                .selfEsteem(req.getSelfEsteem())
                .mentalHealthHistory(req.getMentalHealthHistory())
                .depression(req.getDepression())
                .headache(req.getHeadache())
                .bloodPressure(req.getBloodPressure())
                .sleepQuality(req.getSleepQuality())
                .breathingProblem(req.getBreathingProblem())
                .noiseLevel(req.getNoiseLevel())
                .livingConditions(req.getLivingConditions())
                .safety(req.getSafety())
                .basicNeeds(req.getBasicNeeds())
                .academicPerformance(req.getAcademicPerformance())
                .studyLoad(req.getStudyLoad())
                .teacherStudentRelationship(req.getTeacherStudentRelationship())
                .futureCareerConcerns(req.getFutureCareerConcerns())
                .socialSupport(req.getSocialSupport())
                .peerPressure(req.getPeerPressure())
                .extracurricularActivities(req.getExtracurricularActivities())
                .bullying(req.getBullying())
                .stressLevel(stressLevel)
                .stressLabel(stressLabel)
                .confidence(confidence)
                .lowProbability(probMap != null ? ((Number) probMap.get("Low")).doubleValue() : 0)
                .mediumProbability(probMap != null ? ((Number) probMap.get("Medium")).doubleValue() : 0)
                .highProbability(probMap != null ? ((Number) probMap.get("High")).doubleValue() : 0)
                .recommendations(recommendations != null ? String.join("|", recommendations) : "")
                .topFactors(topFactors != null ? topFactors.toString() : "")
                .build();

        predictionRepository.save(predictionRecord);

        // Build probabilities map
        Map<String, Double> probs = new LinkedHashMap<>();
        if (probMap != null) {
            probs.put("Low", ((Number) probMap.get("Low")).doubleValue());
            probs.put("Medium", ((Number) probMap.get("Medium")).doubleValue());
            probs.put("High", ((Number) probMap.get("High")).doubleValue());
        }

        // Build top factors map
        Map<String, Double> factors = new LinkedHashMap<>();
        if (topFactors != null) {
            topFactors.forEach((k, v) -> factors.put(k, ((Number) v).doubleValue()));
        }

        return PredictionResponse.builder()
                .id(predictionRecord.getId())
                .stressLevel(stressLevel)
                .stressLabel(stressLabel)
                .confidence(confidence)
                .probabilities(probs)
                .recommendations(recommendations)
                .topContributingFactors(factors)
                .createdAt(predictionRecord.getCreatedAt())
                .build();
    }

    public List<PredictionHistoryItem> getUserHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return predictionRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(p -> PredictionHistoryItem.builder()
                        .id(p.getId())
                        .stressLabel(p.getStressLabel())
                        .stressLevel(p.getStressLevel())
                        .confidence(p.getConfidence())
                        .createdAt(p.getCreatedAt())
                        .build())
                .toList();
    }

    public PredictionResponse getPredictionById(Long id, String username) {
        PredictionRecord p = predictionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prediction not found"));

        if (!p.getUser().getUsername().equals(username))
            throw new IllegalArgumentException("Access denied");

        Map<String, Double> probs = Map.of(
                "Low", p.getLowProbability(),
                "Medium", p.getMediumProbability(),
                "High", p.getHighProbability()
        );

        List<String> recs = p.getRecommendations() != null
                ? Arrays.asList(p.getRecommendations().split("\\|"))
                : List.of();

        return PredictionResponse.builder()
                .id(p.getId())
                .stressLevel(p.getStressLevel())
                .stressLabel(p.getStressLabel())
                .confidence(p.getConfidence())
                .probabilities(probs)
                .recommendations(recs)
                .createdAt(p.getCreatedAt())
                .build();
    }

    public UserDashboard getUserDashboard(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<PredictionRecord> all = predictionRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<PredictionRecord> latest = predictionRepository.findLatestByUserId(
                user.getId(), PageRequest.of(0, 5));

        long low = all.stream().filter(p -> "Low".equals(p.getStressLabel())).count();
        long med = all.stream().filter(p -> "Medium".equals(p.getStressLabel())).count();
        long high = all.stream().filter(p -> "High".equals(p.getStressLabel())).count();

        String lastLabel = all.isEmpty() ? "N/A" : all.get(0).getStressLabel();
        Double lastConf = all.isEmpty() ? null : all.get(0).getConfidence();

        List<PredictionHistoryItem> recent = latest.stream()
                .map(p -> PredictionHistoryItem.builder()
                        .id(p.getId()).stressLabel(p.getStressLabel())
                        .stressLevel(p.getStressLevel()).confidence(p.getConfidence())
                        .createdAt(p.getCreatedAt()).build())
                .toList();

        return UserDashboard.builder()
                .totalMyPredictions(all.size())
                .latestStressLabel(lastLabel)
                .latestConfidence(lastConf)
                .myLowCount(low)
                .myMediumCount(med)
                .myHighCount(high)
                .recentHistory(recent)
                .build();
    }

    private Map<String, Object> buildMlInput(PredictionRequest req) {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("anxiety_level", req.getAnxietyLevel());
        input.put("self_esteem", req.getSelfEsteem());
        input.put("mental_health_history", req.getMentalHealthHistory());
        input.put("depression", req.getDepression());
        input.put("headache", req.getHeadache());
        input.put("blood_pressure", req.getBloodPressure());
        input.put("sleep_quality", req.getSleepQuality());
        input.put("breathing_problem", req.getBreathingProblem());
        input.put("noise_level", req.getNoiseLevel());
        input.put("living_conditions", req.getLivingConditions());
        input.put("safety", req.getSafety());
        input.put("basic_needs", req.getBasicNeeds());
        input.put("academic_performance", req.getAcademicPerformance());
        input.put("study_load", req.getStudyLoad());
        input.put("teacher_student_relationship", req.getTeacherStudentRelationship());
        input.put("future_career_concerns", req.getFutureCareerConcerns());
        input.put("social_support", req.getSocialSupport());
        input.put("peer_pressure", req.getPeerPressure());
        input.put("extracurricular_activities", req.getExtracurricularActivities());
        input.put("bullying", req.getBullying());
        return input;
    }
}
