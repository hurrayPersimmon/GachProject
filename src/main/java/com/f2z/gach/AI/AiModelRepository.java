package com.f2z.gach.AI;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AiModelRepository extends JpaRepository<AiModel, Integer> {

    // 마지막에 저장된 ID
    @Query("SELECT am FROM AiModel am WHERE am.aiModelId = (SELECT MAX(am2.aiModelId) FROM AiModel am2)")
    Optional<AiModel> findAiModelWithMaxId();

    Optional<AiModel> findByIsCheckedTrue();
}
