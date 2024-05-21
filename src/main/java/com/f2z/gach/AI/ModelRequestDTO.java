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
    private int depth;
    private int split;
    private int leaf;
    private int maxFeature;
}
