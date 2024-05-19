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
    private int epochs;
    private int hiddenLayer;
    private int numLayer;
    private int batchSize;
    private double learningRate;
    private String aiModelName;
    private String aiModelPath;
    private double mae;
    private double averSatis;

    public static AiModel setToDto(ModelRequestDTO dto){
        AiModel aiModel = new AiModel();
        aiModel.setAiModelName(dto.getModelName());
        aiModel.setAiModelVersion(dto.getModelVersion());
        aiModel.setEpochs(dto.getModelEpochs());
        aiModel.setHiddenLayer(dto.getModelHidden());
        aiModel.setLearningRate(dto.getModelLearning());
        aiModel.setBatchSize(dto.getModelBatch());
        aiModel.setMae(dto.getModelMae());
        aiModel.setNumLayer(dto.getModelLayer());
        return aiModel;
    }
}
