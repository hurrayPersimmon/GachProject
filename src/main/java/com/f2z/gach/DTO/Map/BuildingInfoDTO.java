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
public class BuildingInfoDTO {
    private Integer buildingInfoId;
    private Integer buildingCode;
    private String buildingName;
    private Double buildingHeight;
    private String thumbnailImageName;
    private String thumbnailImagePath;
    private String arImageName;
    private String arImagePath;

}
