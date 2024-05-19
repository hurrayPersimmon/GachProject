package com.f2z.gach.AI;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiModelRepository extends JpaRepository<AiModel, Integer> {
    //Optional<AiModel> findByModelName(String modelName);
}
