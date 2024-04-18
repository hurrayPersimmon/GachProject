package com.f2z.gach.Map.DTOs.Requests;

import com.f2z.gach.EnumType.PlaceCategory;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    private String mainImageName;
    private String mainImagePath;
    private String placeSummary;

}
