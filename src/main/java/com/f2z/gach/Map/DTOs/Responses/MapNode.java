package com.f2z.gach.Map.DTOs.Responses;

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
public class MapNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nodeId;
    private String nodeName;
    private Double nodeLatitude;
    private Double nodeLongitude;
    private Double nodeAltitude;

}
