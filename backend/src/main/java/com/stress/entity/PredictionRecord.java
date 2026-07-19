package com.stress.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_records")
public class PredictionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Input Features
    private Integer anxietyLevel;
    private Integer selfEsteem;
    private Integer mentalHealthHistory;
    private Integer depression;
    private Integer headache;
    private Integer bloodPressure;
    private Integer sleepQuality;
    private Integer breathingProblem;
    private Integer noiseLevel;
    private Integer livingConditions;
    private Integer safety;
    private Integer basicNeeds;
    private Integer academicPerformance;
    private Integer studyLoad;
    private Integer teacherStudentRelationship;
    private Integer futureCareerConcerns;
    private Integer socialSupport;
    private Integer peerPressure;
    private Integer extracurricularActivities;
    private Integer bullying;

    // Prediction Results
    private Integer stressLevel;
    private String stressLabel;
    private Double confidence;
    private Double lowProbability;
    private Double mediumProbability;
    private Double highProbability;

    @Column(columnDefinition = "TEXT")
    private String recommendations;

    @Column(columnDefinition = "TEXT")
    private String topFactors;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public PredictionRecord() {}

    public PredictionRecord(User user, Integer anxietyLevel, Integer selfEsteem, Integer mentalHealthHistory, Integer depression, Integer headache, Integer bloodPressure, Integer sleepQuality, Integer breathingProblem, Integer noiseLevel, Integer livingConditions, Integer safety, Integer basicNeeds, Integer academicPerformance, Integer studyLoad, Integer teacherStudentRelationship, Integer futureCareerConcerns, Integer socialSupport, Integer peerPressure, Integer extracurricularActivities, Integer bullying, Integer stressLevel, String stressLabel, Double confidence, Double lowProbability, Double mediumProbability, Double highProbability, String recommendations, String topFactors) {
        this.user = user;
        this.anxietyLevel = anxietyLevel;
        this.selfEsteem = selfEsteem;
        this.mentalHealthHistory = mentalHealthHistory;
        this.depression = depression;
        this.headache = headache;
        this.bloodPressure = bloodPressure;
        this.sleepQuality = sleepQuality;
        this.breathingProblem = breathingProblem;
        this.noiseLevel = noiseLevel;
        this.livingConditions = livingConditions;
        this.safety = safety;
        this.basicNeeds = basicNeeds;
        this.academicPerformance = academicPerformance;
        this.studyLoad = studyLoad;
        this.teacherStudentRelationship = teacherStudentRelationship;
        this.futureCareerConcerns = futureCareerConcerns;
        this.socialSupport = socialSupport;
        this.peerPressure = peerPressure;
        this.extracurricularActivities = extracurricularActivities;
        this.bullying = bullying;
        this.stressLevel = stressLevel;
        this.stressLabel = stressLabel;
        this.confidence = confidence;
        this.lowProbability = lowProbability;
        this.mediumProbability = mediumProbability;
        this.highProbability = highProbability;
        this.recommendations = recommendations;
        this.topFactors = topFactors;
    }

    // Builder pattern
    public static PredictionRecordBuilder builder() {
        return new PredictionRecordBuilder();
    }

    public static class PredictionRecordBuilder {
        private Long id;
        private User user;
        private Integer anxietyLevel;
        private Integer selfEsteem;
        private Integer mentalHealthHistory;
        private Integer depression;
        private Integer headache;
        private Integer bloodPressure;
        private Integer sleepQuality;
        private Integer breathingProblem;
        private Integer noiseLevel;
        private Integer livingConditions;
        private Integer safety;
        private Integer basicNeeds;
        private Integer academicPerformance;
        private Integer studyLoad;
        private Integer teacherStudentRelationship;
        private Integer futureCareerConcerns;
        private Integer socialSupport;
        private Integer peerPressure;
        private Integer extracurricularActivities;
        private Integer bullying;
        private Integer stressLevel;
        private String stressLabel;
        private Double confidence;
        private Double lowProbability;
        private Double mediumProbability;
        private Double highProbability;
        private String recommendations;
        private String topFactors;
        private LocalDateTime createdAt;

        public PredictionRecordBuilder id(Long id) { this.id = id; return this; }
        public PredictionRecordBuilder user(User user) { this.user = user; return this; }
        public PredictionRecordBuilder anxietyLevel(Integer anxietyLevel) { this.anxietyLevel = anxietyLevel; return this; }
        public PredictionRecordBuilder selfEsteem(Integer selfEsteem) { this.selfEsteem = selfEsteem; return this; }
        public PredictionRecordBuilder mentalHealthHistory(Integer mentalHealthHistory) { this.mentalHealthHistory = mentalHealthHistory; return this; }
        public PredictionRecordBuilder depression(Integer depression) { this.depression = depression; return this; }
        public PredictionRecordBuilder headache(Integer headache) { this.headache = headache; return this; }
        public PredictionRecordBuilder bloodPressure(Integer bloodPressure) { this.bloodPressure = bloodPressure; return this; }
        public PredictionRecordBuilder sleepQuality(Integer sleepQuality) { this.sleepQuality = sleepQuality; return this; }
        public PredictionRecordBuilder breathingProblem(Integer breathingProblem) { this.breathingProblem = breathingProblem; return this; }
        public PredictionRecordBuilder noiseLevel(Integer noiseLevel) { this.noiseLevel = noiseLevel; return this; }
        public PredictionRecordBuilder livingConditions(Integer livingConditions) { this.livingConditions = livingConditions; return this; }
        public PredictionRecordBuilder safety(Integer safety) { this.safety = safety; return this; }
        public PredictionRecordBuilder basicNeeds(Integer basicNeeds) { this.basicNeeds = basicNeeds; return this; }
        public PredictionRecordBuilder academicPerformance(Integer academicPerformance) { this.academicPerformance = academicPerformance; return this; }
        public PredictionRecordBuilder studyLoad(Integer studyLoad) { this.studyLoad = studyLoad; return this; }
        public PredictionRecordBuilder teacherStudentRelationship(Integer teacherStudentRelationship) { this.teacherStudentRelationship = teacherStudentRelationship; return this; }
        public PredictionRecordBuilder futureCareerConcerns(Integer futureCareerConcerns) { this.futureCareerConcerns = futureCareerConcerns; return this; }
        public PredictionRecordBuilder socialSupport(Integer socialSupport) { this.socialSupport = socialSupport; return this; }
        public PredictionRecordBuilder peerPressure(Integer peerPressure) { this.peerPressure = peerPressure; return this; }
        public PredictionRecordBuilder extracurricularActivities(Integer extracurricularActivities) { this.extracurricularActivities = extracurricularActivities; return this; }
        public PredictionRecordBuilder bullying(Integer bullying) { this.bullying = bullying; return this; }
        public PredictionRecordBuilder stressLevel(Integer stressLevel) { this.stressLevel = stressLevel; return this; }
        public PredictionRecordBuilder stressLabel(String stressLabel) { this.stressLabel = stressLabel; return this; }
        public PredictionRecordBuilder confidence(Double confidence) { this.confidence = confidence; return this; }
        public PredictionRecordBuilder lowProbability(Double lowProbability) { this.lowProbability = lowProbability; return this; }
        public PredictionRecordBuilder mediumProbability(Double mediumProbability) { this.mediumProbability = mediumProbability; return this; }
        public PredictionRecordBuilder highProbability(Double highProbability) { this.highProbability = highProbability; return this; }
        public PredictionRecordBuilder recommendations(String recommendations) { this.recommendations = recommendations; return this; }
        public PredictionRecordBuilder topFactors(String topFactors) { this.topFactors = topFactors; return this; }
        public PredictionRecordBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public PredictionRecord build() {
            PredictionRecord record = new PredictionRecord();
            record.id = this.id;
            record.user = this.user;
            record.anxietyLevel = this.anxietyLevel;
            record.selfEsteem = this.selfEsteem;
            record.mentalHealthHistory = this.mentalHealthHistory;
            record.depression = this.depression;
            record.headache = this.headache;
            record.bloodPressure = this.bloodPressure;
            record.sleepQuality = this.sleepQuality;
            record.breathingProblem = this.breathingProblem;
            record.noiseLevel = this.noiseLevel;
            record.livingConditions = this.livingConditions;
            record.safety = this.safety;
            record.basicNeeds = this.basicNeeds;
            record.academicPerformance = this.academicPerformance;
            record.studyLoad = this.studyLoad;
            record.teacherStudentRelationship = this.teacherStudentRelationship;
            record.futureCareerConcerns = this.futureCareerConcerns;
            record.socialSupport = this.socialSupport;
            record.peerPressure = this.peerPressure;
            record.extracurricularActivities = this.extracurricularActivities;
            record.bullying = this.bullying;
            record.stressLevel = this.stressLevel;
            record.stressLabel = this.stressLabel;
            record.confidence = this.confidence;
            record.lowProbability = this.lowProbability;
            record.mediumProbability = this.mediumProbability;
            record.highProbability = this.highProbability;
            record.recommendations = this.recommendations;
            record.topFactors = this.topFactors;
            record.createdAt = this.createdAt;
            return record;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public Integer getStressLevel() { return stressLevel; }
    public void setStressLevel(Integer stressLevel) { this.stressLevel = stressLevel; }

    public String getStressLabel() { return stressLabel; }
    public void setStressLabel(String stressLabel) { this.stressLabel = stressLabel; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public Double getLowProbability() { return lowProbability; }
    public void setLowProbability(Double lowProbability) { this.lowProbability = lowProbability; }

    public Double getMediumProbability() { return mediumProbability; }
    public void setMediumProbability(Double mediumProbability) { this.mediumProbability = mediumProbability; }

    public Double getHighProbability() { return highProbability; }
    public void setHighProbability(Double highProbability) { this.highProbability = highProbability; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public String getTopFactors() { return topFactors; }
    public void setTopFactors(String topFactors) { this.topFactors = topFactors; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
