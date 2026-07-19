package com.stress.repository;

import com.stress.entity.PredictionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;


public interface PredictionRepository extends JpaRepository<PredictionRecord, Long> {
    List<PredictionRecord> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<PredictionRecord> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
        Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(p) FROM PredictionRecord p WHERE p.stressLabel = :label")
    long countByStressLabel(@Param("label") String label);

    @Query("SELECT p.stressLabel, COUNT(p) FROM PredictionRecord p GROUP BY p.stressLabel")
    List<Object[]> countByStressLevelGrouped();

    @Query("SELECT p FROM PredictionRecord p ORDER BY p.createdAt DESC")
    List<PredictionRecord> findRecentPredictions(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT AVG(p.confidence) FROM PredictionRecord p")
    Double averageConfidence();

    @Query("SELECT COUNT(DISTINCT p.user.id) FROM PredictionRecord p")
    long countUsersWithPredictions();

    @Query("SELECT p FROM PredictionRecord p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    List<PredictionRecord> findLatestByUserId(@Param("userId") Long userId,
        org.springframework.data.domain.Pageable pageable);
}
