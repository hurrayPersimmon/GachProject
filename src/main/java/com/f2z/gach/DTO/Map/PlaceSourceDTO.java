package com.f2z.gach.DTO.Map;

import com.f2z.gach.EnumType.PlaceCategory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlaceSourceDTO {
    private Integer placeId;
    private String placeName;
    @Enumerated(EnumType.STRING)
    private PlaceCategory placeCategory;
    private Double placeLatitude;
    private Double placeLongitude;
    private Double placeAltitude;
    private String mainImageName;
    private String mainImagePath;
    private String placeSummary;

}
