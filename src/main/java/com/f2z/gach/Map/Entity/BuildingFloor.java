package com.f2z.gach.Map.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class BuildingFloor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buildingFloorId;
    private String buildingName;
    private String buildingFloor;
    private String buildingFloorInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeId", updatable = false)
    private PlaceSource placeSource;

    public static BuildingFloor updateBuildingFloor(BuildingFloor buildingFloor, PlaceSource placeSource) {
        return BuildingFloor.builder()
                .buildingFloorId(buildingFloor.getBuildingFloorId() != null ? buildingFloor.getBuildingFloorId() : null)
                .buildingName(placeSource.getPlaceName())
                .buildingFloor(buildingFloor.getBuildingFloor())
                .buildingFloorInfo(buildingFloor.getBuildingFloorInfo())
                .placeSource(placeSource)
                .build();
    }

}
