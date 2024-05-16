package com.f2z.gach.Map.Entity;

import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Map.DTO.MapDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Slf4j
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


    private String mainImageName;
    private String mainImagePath;
    private String thumbnailImageName;
    private String thumbnailImagePath;
    private String arImageName;
    private String arImagePath;

    public void update(PlaceSource placeSource) {
        log.info("update placeSource: {}", placeSource.toString());
        this.placeName = placeSource.getPlaceName();
        this.placeCategory = placeSource.getPlaceCategory();
        this.placeLatitude = placeSource.getPlaceLatitude();
        this.placeLongitude = placeSource.getPlaceLongitude();
        this.placeAltitude = placeSource.getPlaceAltitude();
        if(placeSource.getPlaceSummary() == null) this.placeSummary = " ";
        else this.placeSummary = placeSource.getPlaceSummary();

        this.buildingHeight = placeSource.getBuildingHeight();
        if(placeSource.getMainImageName() !=null) this.mainImageName = placeSource.getMainImageName();
        if(placeSource.getMainImagePath() !=null) this.mainImagePath = placeSource.getMainImagePath();
        if(placeSource.getThumbnailImageName() !=null) this.thumbnailImageName = placeSource.getThumbnailImageName();
        if(placeSource.getThumbnailImagePath() !=null) this.thumbnailImagePath = placeSource.getThumbnailImagePath();
        if(placeSource.getArImageName() !=null) this.arImageName = placeSource.getArImageName();
        if(placeSource.getArImagePath() !=null) this.arImagePath = placeSource.getArImagePath();
    }



}
