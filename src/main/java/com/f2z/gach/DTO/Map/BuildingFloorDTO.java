package com.f2z.gach.DTO.Map;

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
public class BuildingFloorDTO {
    private Integer buildingFloorId;
    private Integer buildingCode;
    private String buildingName;
    private String buildingFloor;
    private String buildingFloorInfo;



}
