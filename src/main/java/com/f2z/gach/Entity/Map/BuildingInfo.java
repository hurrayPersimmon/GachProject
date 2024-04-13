package com.f2z.gach.Entity.Map;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class BuildingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buildingInfoId;
    private Integer buildingCode;
    private String buildingName;
    private Double buildingHeight;
    private String thumbnailImageName;
    private String thumbnailImagePath;
    private String arImageName;
    private String arImagePath;

}
