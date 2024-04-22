package com.f2z.gach.Map.Entity;

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
    @Lob
    private byte[] thumbnailImage;
    @Lob
    private byte[] arImage;

}
