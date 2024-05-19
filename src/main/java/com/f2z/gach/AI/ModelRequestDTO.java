package com.f2z.gach.AI;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModelRequestDTO {
    private String modelName;
    private double modelMae;
    private String modelVersion;
    private int modelEpochs;
    private int modelBatch;
    private int modelHidden;
    private int modelLayer;
    private double modelLearning;
}
