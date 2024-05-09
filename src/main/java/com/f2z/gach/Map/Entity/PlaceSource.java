package com.f2z.gach.Map.Entity;

import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Map.DTO.MapDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
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

    public void update(PlaceSource placeSource) {
        this.placeName = placeSource.getPlaceName();
        this.placeCategory = placeSource.getPlaceCategory();
        this.placeLatitude = placeSource.getPlaceLatitude();
        this.placeLongitude = placeSource.getPlaceLongitude();
        this.placeAltitude = placeSource.getPlaceAltitude();
        this.placeSummary = placeSource.getPlaceSummary();
        this.buildingHeight = placeSource.getBuildingHeight();
        this.buildingFloors = placeSource.getBuildingFloors();
        this.mainImageName = placeSource.getMainImageName();
        this.mainImagePath = placeSource.getMainImagePath();
        this.thumbnailImageName = placeSource.getThumbnailImageName();
        this.thumbnailImagePath = placeSource.getThumbnailImagePath();
        this.arImageName = placeSource.getArImageName();
        this.arImagePath = placeSource.getArImagePath();
    }



}
