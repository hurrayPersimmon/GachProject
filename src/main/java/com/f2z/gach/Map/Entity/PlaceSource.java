package com.f2z.gach.Map.Entity;

import com.f2z.gach.EnumType.PlaceCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class PlaceSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer placeId;
    private String placeName;
    @Enumerated(EnumType.STRING)
    private PlaceCategory placeCategory;
    private Double placeLatitude;
    private Double placeLongitude;
    private Double placeAltitude;
    private String placeSummary;
    private Double buildingHeight;
    @OneToMany
    @JoinColumn(name="building_floor_id")
    private List<BuildingFloor> buildingFloors;

    private String mainImageName;
    private String mainImagePath;
    private String thumbnailImageName;
    private String thumbnailImagePath;
    private String arImageName;
    private String arImagePath;
}
