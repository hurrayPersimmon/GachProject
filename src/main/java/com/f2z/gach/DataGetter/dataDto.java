package com.f2z.gach.DataGetter;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class dataDto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;
    private String node1;
    private String node2;
    private Integer birthYear;
    private Integer gender;
    private Double height;
    private Double weight;
    private Integer walkSpeed;
    private Double temperature;
    private Integer precipitationProbability;
    private Double precipitation;
    private Integer takeTime;
    private Double weightShortest;
    private Double weightOptimal;

    public dataEntity toEntity() {
        return new dataEntity(dataId, node1, node2, birthYear, gender,
                height, weight, walkSpeed, temperature, precipitationProbability,
                precipitation, takeTime, weightShortest, weightOptimal);
    }
}