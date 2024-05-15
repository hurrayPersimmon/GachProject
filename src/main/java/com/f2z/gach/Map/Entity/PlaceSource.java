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
        if(mainImageName !=null)this.mainImageName = placeSource.getMainImageName();
        if(mainImagePath !=null) this.mainImagePath = placeSource.getMainImagePath();
        if(thumbnailImageName !=null) this.thumbnailImageName = placeSource.getThumbnailImageName();
        if(thumbnailImagePath !=null) this.thumbnailImagePath = placeSource.getThumbnailImagePath();
        if(arImageName !=null) this.arImageName = placeSource.getArImageName();
        if(arImagePath !=null) this.arImagePath = placeSource.getArImagePath();
    }



}
