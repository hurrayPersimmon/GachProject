package com.f2z.gach.AI;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class AiModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aiModelId;
    private String aiModelVersion;
    private int maxDepth;
    private int minSampleSplit;
    private int minSampleLeaf;
    private int maxFeature;
    private String aiModelName;
    private String aiModelPath;
    private double mae;
    private double averSatis;
    private long lastDataIndex;

    public static AiModel setToDto(ModelRequestDTO dto){
        AiModel aiModel = new AiModel();
        aiModel.setAiModelName(dto.getModelName());
        aiModel.setAiModelVersion(dto.getModelVersion());
        aiModel.setMaxDepth(dto.getDepth());
        aiModel.setMinSampleSplit(dto.getSplit());
        aiModel.setMinSampleLeaf(dto.getLeaf());
        aiModel.setMaxFeature(dto.getMaxFeature());
        aiModel.setMae(dto.getModelMae());
        return aiModel;
    }
}
