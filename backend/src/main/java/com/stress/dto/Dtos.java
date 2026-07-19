package com.stress.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Dtos {
    private Dtos() {}


    // ─── Auth ────────────────────────────────────────────────────────────
    public static class RegisterRequest {
        @NotBlank public String username;
        @NotBlank @Email public String email;
        @NotBlank @Size(min = 6) public String password;
        public String fullName;
        public Integer age;
        public String gender;
        public String phone;

        public RegisterRequest() {}
        public RegisterRequest(String username, String email, String password, String fullName, Integer age, String gender, String phone) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.fullName = fullName;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }

    public static class LoginRequest {
        @NotBlank public String username;
        @NotBlank public String password;

        public LoginRequest() {}
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        public String token;
        public String username;
        public String email;
        public String fullName;
        public String role;
        public Long userId;

        public AuthResponse() {}
        public AuthResponse(String token, String username, String email, String fullName, String role, Long userId) {
            this.token = token;
            this.username = username;
            this.email = email;
            this.fullName = fullName;
            this.role = role;
            this.userId = userId;
        }

        public static AuthResponseBuilder builder() {
            return new AuthResponseBuilder();
        }

        public static class AuthResponseBuilder {
            private String token;
            private String username;
            private String email;
            private String fullName;
            private String role;
            private Long userId;

            public AuthResponseBuilder token(String token) { this.token = token; return this; }
            public AuthResponseBuilder username(String username) { this.username = username; return this; }
            public AuthResponseBuilder email(String email) { this.email = email; return this; }
            public AuthResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public AuthResponseBuilder role(String role) { this.role = role; return this; }
            public AuthResponseBuilder userId(Long userId) { this.userId = userId; return this; }

            public AuthResponse build() {
                return new AuthResponse(token, username, email, fullName, role, userId);
            }
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    // ─── User ────────────────────────────────────────────────────────────
    public static class UserResponse {
        public Long id;
        public String username;
        public String email;
        public String fullName;
        public Integer age;
        public String gender;
        public String phone;
        public String role;
        public Boolean active;
        public LocalDateTime createdAt;
        public int totalPredictions;
        public String lastStressLabel;

        public UserResponse() {}
        public UserResponse(Long id, String username, String email, String fullName, Integer age, String gender, String phone, String role, Boolean active, LocalDateTime createdAt, int totalPredictions, String lastStressLabel) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.fullName = fullName;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
            this.role = role;
            this.active = active;
            this.createdAt = createdAt;
            this.totalPredictions = totalPredictions;
            this.lastStressLabel = lastStressLabel;
        }

        public static UserResponseBuilder builder() {
            return new UserResponseBuilder();
        }

        public static class UserResponseBuilder {
            private Long id;
            private String username;
            private String email;
            private String fullName;
            private Integer age;
            private String gender;
            private String phone;
            private String role;
            private Boolean active;
            private LocalDateTime createdAt;
            private int totalPredictions;
            private String lastStressLabel;

            public UserResponseBuilder id(Long id) { this.id = id; return this; }
            public UserResponseBuilder username(String username) { this.username = username; return this; }
            public UserResponseBuilder email(String email) { this.email = email; return this; }
            public UserResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public UserResponseBuilder age(Integer age) { this.age = age; return this; }
            public UserResponseBuilder gender(String gender) { this.gender = gender; return this; }
            public UserResponseBuilder phone(String phone) { this.phone = phone; return this; }
            public UserResponseBuilder role(String role) { this.role = role; return this; }
            public UserResponseBuilder active(Boolean active) { this.active = active; return this; }
            public UserResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public UserResponseBuilder totalPredictions(int totalPredictions) { this.totalPredictions = totalPredictions; return this; }
            public UserResponseBuilder lastStressLabel(String lastStressLabel) { this.lastStressLabel = lastStressLabel; return this; }

            public UserResponse build() {
                return new UserResponse(id, username, email, fullName, age, gender, phone, role, active, createdAt, totalPredictions, lastStressLabel);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public int getTotalPredictions() { return totalPredictions; }
        public void setTotalPredictions(int totalPredictions) { this.totalPredictions = totalPredictions; }
        public String getLastStressLabel() { return lastStressLabel; }
        public void setLastStressLabel(String lastStressLabel) { this.lastStressLabel = lastStressLabel; }
    }

    public static class UpdateUserRequest {
        public String fullName;
        public Integer age;
        public String gender;
        public String phone;
        public String email;

        public UpdateUserRequest() {}
        public UpdateUserRequest(String fullName, Integer age, String gender, String phone, String email) {
            this.fullName = fullName;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
            this.email = email;
        }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    // ─── Prediction ──────────────────────────────────────────────────────
    public static class PredictionRequest {
        @NotNull @Min(0) @Max(21) public Integer anxietyLevel;
        @NotNull @Min(0) @Max(30) public Integer selfEsteem;
        @NotNull @Min(0) @Max(1)  public Integer mentalHealthHistory;
        @NotNull @Min(0) @Max(27) public Integer depression;
        @NotNull @Min(0) @Max(5)  public Integer headache;
        @NotNull @Min(0) @Max(3)  public Integer bloodPressure;
        @NotNull @Min(0) @Max(9)  public Integer sleepQuality;
        @NotNull @Min(0) @Max(5)  public Integer breathingProblem;
        @NotNull @Min(0) @Max(7)  public Integer noiseLevel;
        @NotNull @Min(0) @Max(5)  public Integer livingConditions;
        @NotNull @Min(0) @Max(5)  public Integer safety;
        @NotNull @Min(0) @Max(5)  public Integer basicNeeds;
        @NotNull @Min(0) @Max(5)  public Integer academicPerformance;
        @NotNull @Min(0) @Max(5)  public Integer studyLoad;
        @NotNull @Min(0) @Max(5)  public Integer teacherStudentRelationship;
        @NotNull @Min(0) @Max(5)  public Integer futureCareerConcerns;
        @NotNull @Min(0) @Max(3)  public Integer socialSupport;
        @NotNull @Min(0) @Max(5)  public Integer peerPressure;
        @NotNull @Min(0) @Max(5)  public Integer extracurricularActivities;
        @NotNull @Min(0) @Max(5)  public Integer bullying;

        public PredictionRequest() {
            // Bean-compatible no-args constructor for deserialization
        }

        public Integer getAnxietyLevel() { return anxietyLevel; }
        public void setAnxietyLevel(Integer anxietyLevel) { this.anxietyLevel = anxietyLevel; }
        public Integer getSelfEsteem() { return selfEsteem; }
        public void setSelfEsteem(Integer selfEsteem) { this.selfEsteem = selfEsteem; }
        public Integer getMentalHealthHistory() { return mentalHealthHistory; }
        public void setMentalHealthHistory(Integer mentalHealthHistory) { this.mentalHealthHistory = mentalHealthHistory; }
        public Integer getDepression() { return depression; }
        public void setDepression(Integer depression) { this.depression = depression; }
        public Integer getHeadache() { return headache; }
        public void setHeadache(Integer headache) { this.headache = headache; }
        public Integer getBloodPressure() { return bloodPressure; }
        public void setBloodPressure(Integer bloodPressure) { this.bloodPressure = bloodPressure; }
        public Integer getSleepQuality() { return sleepQuality; }
        public void setSleepQuality(Integer sleepQuality) { this.sleepQuality = sleepQuality; }
        public Integer getBreathingProblem() { return breathingProblem; }
        public void setBreathingProblem(Integer breathingProblem) { this.breathingProblem = breathingProblem; }
        public Integer getNoiseLevel() { return noiseLevel; }
        public void setNoiseLevel(Integer noiseLevel) { this.noiseLevel = noiseLevel; }
        public Integer getLivingConditions() { return livingConditions; }
        public void setLivingConditions(Integer livingConditions) { this.livingConditions = livingConditions; }
        public Integer getSafety() { return safety; }
        public void setSafety(Integer safety) { this.safety = safety; }
        public Integer getBasicNeeds() { return basicNeeds; }
        public void setBasicNeeds(Integer basicNeeds) { this.basicNeeds = basicNeeds; }
        public Integer getAcademicPerformance() { return academicPerformance; }
        public void setAcademicPerformance(Integer academicPerformance) { this.academicPerformance = academicPerformance; }
        public Integer getStudyLoad() { return studyLoad; }
        public void setStudyLoad(Integer studyLoad) { this.studyLoad = studyLoad; }
        public Integer getTeacherStudentRelationship() { return teacherStudentRelationship; }
        public void setTeacherStudentRelationship(Integer teacherStudentRelationship) { this.teacherStudentRelationship = teacherStudentRelationship; }
        public Integer getFutureCareerConcerns() { return futureCareerConcerns; }
        public void setFutureCareerConcerns(Integer futureCareerConcerns) { this.futureCareerConcerns = futureCareerConcerns; }
        public Integer getSocialSupport() { return socialSupport; }
        public void setSocialSupport(Integer socialSupport) { this.socialSupport = socialSupport; }
        public Integer getPeerPressure() { return peerPressure; }
        public void setPeerPressure(Integer peerPressure) { this.peerPressure = peerPressure; }
        public Integer getExtracurricularActivities() { return extracurricularActivities; }
        public void setExtracurricularActivities(Integer extracurricularActivities) { this.extracurricularActivities = extracurricularActivities; }
        public Integer getBullying() { return bullying; }
        public void setBullying(Integer bullying) { this.bullying = bullying; }
    }

    public static class PredictionResponse {
        public Long id;
        public Integer stressLevel;
        public String stressLabel;
        public Double confidence;
        public Map<String, Double> probabilities;
        public List<String> recommendations;
        public Map<String, Double> topContributingFactors;
        public LocalDateTime createdAt;

        public PredictionResponse() {}
        public PredictionResponse(Long id, Integer stressLevel, String stressLabel, Double confidence, Map<String, Double> probabilities, List<String> recommendations, Map<String, Double> topContributingFactors, LocalDateTime createdAt) {
            this.id = id;
            this.stressLevel = stressLevel;
            this.stressLabel = stressLabel;
            this.confidence = confidence;
            this.probabilities = probabilities;
            this.recommendations = recommendations;
            this.topContributingFactors = topContributingFactors;
            this.createdAt = createdAt;
        }

        public static PredictionResponseBuilder builder() {
            return new PredictionResponseBuilder();
        }

        public static class PredictionResponseBuilder {
            private Long id;
            private Integer stressLevel;
            private String stressLabel;
            private Double confidence;
            private Map<String, Double> probabilities;
            private List<String> recommendations;
            private Map<String, Double> topContributingFactors;
            private LocalDateTime createdAt;

            public PredictionResponseBuilder id(Long id) { this.id = id; return this; }
            public PredictionResponseBuilder stressLevel(Integer stressLevel) { this.stressLevel = stressLevel; return this; }
            public PredictionResponseBuilder stressLabel(String stressLabel) { this.stressLabel = stressLabel; return this; }
            public PredictionResponseBuilder confidence(Double confidence) { this.confidence = confidence; return this; }
            public PredictionResponseBuilder probabilities(Map<String, Double> probabilities) { this.probabilities = probabilities; return this; }
            public PredictionResponseBuilder recommendations(List<String> recommendations) { this.recommendations = recommendations; return this; }
            public PredictionResponseBuilder topContributingFactors(Map<String, Double> topContributingFactors) { this.topContributingFactors = topContributingFactors; return this; }
            public PredictionResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public PredictionResponse build() {
                return new PredictionResponse(id, stressLevel, stressLabel, confidence, probabilities, recommendations, topContributingFactors, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Integer getStressLevel() { return stressLevel; }
        public void setStressLevel(Integer stressLevel) { this.stressLevel = stressLevel; }
        public String getStressLabel() { return stressLabel; }
        public void setStressLabel(String stressLabel) { this.stressLabel = stressLabel; }
        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }
        public Map<String, Double> getProbabilities() { return probabilities; }
        public void setProbabilities(Map<String, Double> probabilities) { this.probabilities = probabilities; }
        public List<String> getRecommendations() { return recommendations; }
        public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
        public Map<String, Double> getTopContributingFactors() { return topContributingFactors; }
        public void setTopContributingFactors(Map<String, Double> topContributingFactors) { this.topContributingFactors = topContributingFactors; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class PredictionHistoryItem {
        public Long id;
        public String stressLabel;
        public Integer stressLevel;
        public Double confidence;
        public LocalDateTime createdAt;

        public PredictionHistoryItem() {}
        public PredictionHistoryItem(Long id, String stressLabel, Integer stressLevel, Double confidence, LocalDateTime createdAt) {
            this.id = id;
            this.stressLabel = stressLabel;
            this.stressLevel = stressLevel;
            this.confidence = confidence;
            this.createdAt = createdAt;
        }

        public static PredictionHistoryItemBuilder builder() {
            return new PredictionHistoryItemBuilder();
        }

        public static class PredictionHistoryItemBuilder {
            private Long id;
            private String stressLabel;
            private Integer stressLevel;
            private Double confidence;
            private LocalDateTime createdAt;

            public PredictionHistoryItemBuilder id(Long id) { this.id = id; return this; }
            public PredictionHistoryItemBuilder stressLabel(String stressLabel) { this.stressLabel = stressLabel; return this; }
            public PredictionHistoryItemBuilder stressLevel(Integer stressLevel) { this.stressLevel = stressLevel; return this; }
            public PredictionHistoryItemBuilder confidence(Double confidence) { this.confidence = confidence; return this; }
            public PredictionHistoryItemBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public PredictionHistoryItem build() {
                return new PredictionHistoryItem(id, stressLabel, stressLevel, confidence, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getStressLabel() { return stressLabel; }
        public void setStressLabel(String stressLabel) { this.stressLabel = stressLabel; }
        public Integer getStressLevel() { return stressLevel; }
        public void setStressLevel(Integer stressLevel) { this.stressLevel = stressLevel; }
        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    // ─── Dashboard ───────────────────────────────────────────────────────
    public static class DashboardStats {
        public long totalUsers;
        public long totalPredictions;
        public long lowStressCount;
        public long mediumStressCount;
        public long highStressCount;
        public double averageConfidence;
        public long activeUsersWithPredictions;

        public DashboardStats() {}
        public DashboardStats(long totalUsers, long totalPredictions, long lowStressCount, long mediumStressCount, long highStressCount, double averageConfidence, long activeUsersWithPredictions) {
            this.totalUsers = totalUsers;
            this.totalPredictions = totalPredictions;
            this.lowStressCount = lowStressCount;
            this.mediumStressCount = mediumStressCount;
            this.highStressCount = highStressCount;
            this.averageConfidence = averageConfidence;
            this.activeUsersWithPredictions = activeUsersWithPredictions;
        }

        public static DashboardStatsBuilder builder() {
            return new DashboardStatsBuilder();
        }

        public static class DashboardStatsBuilder {
            private long totalUsers;
            private long totalPredictions;
            private long lowStressCount;
            private long mediumStressCount;
            private long highStressCount;
            private double averageConfidence;
            private long activeUsersWithPredictions;

            public DashboardStatsBuilder totalUsers(long totalUsers) { this.totalUsers = totalUsers; return this; }
            public DashboardStatsBuilder totalPredictions(long totalPredictions) { this.totalPredictions = totalPredictions; return this; }
            public DashboardStatsBuilder lowStressCount(long lowStressCount) { this.lowStressCount = lowStressCount; return this; }
            public DashboardStatsBuilder mediumStressCount(long mediumStressCount) { this.mediumStressCount = mediumStressCount; return this; }
            public DashboardStatsBuilder highStressCount(long highStressCount) { this.highStressCount = highStressCount; return this; }
            public DashboardStatsBuilder averageConfidence(double averageConfidence) { this.averageConfidence = averageConfidence; return this; }
            public DashboardStatsBuilder activeUsersWithPredictions(long activeUsersWithPredictions) { this.activeUsersWithPredictions = activeUsersWithPredictions; return this; }

            public DashboardStats build() {
                return new DashboardStats(totalUsers, totalPredictions, lowStressCount, mediumStressCount, highStressCount, averageConfidence, activeUsersWithPredictions);
            }
        }

        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getTotalPredictions() { return totalPredictions; }
        public void setTotalPredictions(long totalPredictions) { this.totalPredictions = totalPredictions; }
        public long getLowStressCount() { return lowStressCount; }
        public void setLowStressCount(long lowStressCount) { this.lowStressCount = lowStressCount; }
        public long getMediumStressCount() { return mediumStressCount; }
        public void setMediumStressCount(long mediumStressCount) { this.mediumStressCount = mediumStressCount; }
        public long getHighStressCount() { return highStressCount; }
        public void setHighStressCount(long highStressCount) { this.highStressCount = highStressCount; }
        public double getAverageConfidence() { return averageConfidence; }
        public void setAverageConfidence(double averageConfidence) { this.averageConfidence = averageConfidence; }
        public long getActiveUsersWithPredictions() { return activeUsersWithPredictions; }
        public void setActiveUsersWithPredictions(long activeUsersWithPredictions) { this.activeUsersWithPredictions = activeUsersWithPredictions; }
    }

    public static class UserDashboard {
        public int totalMyPredictions;
        public String latestStressLabel;
        public Double latestConfidence;
        public long myLowCount;
        public long myMediumCount;
        public long myHighCount;
        public List<PredictionHistoryItem> recentHistory;

        public UserDashboard() {}
        public UserDashboard(int totalMyPredictions, String latestStressLabel, Double latestConfidence, long myLowCount, long myMediumCount, long myHighCount, List<PredictionHistoryItem> recentHistory) {
            this.totalMyPredictions = totalMyPredictions;
            this.latestStressLabel = latestStressLabel;
            this.latestConfidence = latestConfidence;
            this.myLowCount = myLowCount;
            this.myMediumCount = myMediumCount;
            this.myHighCount = myHighCount;
            this.recentHistory = recentHistory;
        }

        public static UserDashboardBuilder builder() {
            return new UserDashboardBuilder();
        }

        public static class UserDashboardBuilder {
            private int totalMyPredictions;
            private String latestStressLabel;
            private Double latestConfidence;
            private long myLowCount;
            private long myMediumCount;
            private long myHighCount;
            private List<PredictionHistoryItem> recentHistory;

            public UserDashboardBuilder totalMyPredictions(int totalMyPredictions) { this.totalMyPredictions = totalMyPredictions; return this; }
            public UserDashboardBuilder latestStressLabel(String latestStressLabel) { this.latestStressLabel = latestStressLabel; return this; }
            public UserDashboardBuilder latestConfidence(Double latestConfidence) { this.latestConfidence = latestConfidence; return this; }
            public UserDashboardBuilder myLowCount(long myLowCount) { this.myLowCount = myLowCount; return this; }
            public UserDashboardBuilder myMediumCount(long myMediumCount) { this.myMediumCount = myMediumCount; return this; }
            public UserDashboardBuilder myHighCount(long myHighCount) { this.myHighCount = myHighCount; return this; }
            public UserDashboardBuilder recentHistory(List<PredictionHistoryItem> recentHistory) { this.recentHistory = recentHistory; return this; }

            public UserDashboard build() {
                return new UserDashboard(totalMyPredictions, latestStressLabel, latestConfidence, myLowCount, myMediumCount, myHighCount, recentHistory);
            }
        }

        public int getTotalMyPredictions() { return totalMyPredictions; }
        public void setTotalMyPredictions(int totalMyPredictions) { this.totalMyPredictions = totalMyPredictions; }
        public String getLatestStressLabel() { return latestStressLabel; }
        public void setLatestStressLabel(String latestStressLabel) { this.latestStressLabel = latestStressLabel; }
        public Double getLatestConfidence() { return latestConfidence; }
        public void setLatestConfidence(Double latestConfidence) { this.latestConfidence = latestConfidence; }
        public long getMyLowCount() { return myLowCount; }
        public void setMyLowCount(long myLowCount) { this.myLowCount = myLowCount; }
        public long getMyMediumCount() { return myMediumCount; }
        public void setMyMediumCount(long myMediumCount) { this.myMediumCount = myMediumCount; }
        public long getMyHighCount() { return myHighCount; }
        public void setMyHighCount(long myHighCount) { this.myHighCount = myHighCount; }
        public List<PredictionHistoryItem> getRecentHistory() { return recentHistory; }
        public void setRecentHistory(List<PredictionHistoryItem> recentHistory) { this.recentHistory = recentHistory; }
    }

    // ─── Admin ───────────────────────────────────────────────────────────
    public static class AdminUpdateUserRequest {
        public Boolean active;
        public String role;
        public String fullName;
        public String email;

        public AdminUpdateUserRequest() {}
        public AdminUpdateUserRequest(Boolean active, String role, String fullName, String email) {
            this.active = active;
            this.role = role;
            this.fullName = fullName;
            this.email = email;
        }

        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
