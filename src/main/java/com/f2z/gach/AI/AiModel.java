package com.f2z.gach.AI;

import com.f2z.gach.Config.BaseTimeEntity;
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
public class AiModel extends BaseTimeEntity {
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
    private long dataLength;
    private double mse;
    private int cnt;
    private double totalSatisfaction;
    private Boolean isChecked;
}
