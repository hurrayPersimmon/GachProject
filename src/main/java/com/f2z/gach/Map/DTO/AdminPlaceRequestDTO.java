package com.f2z.gach.Map.DTO;

import com.f2z.gach.Event.DTO.AdminEventRequestDTO;
import com.f2z.gach.Event.Entity.Event;
import com.f2z.gach.Event.Entity.EventLocation;
import com.f2z.gach.Map.Entity.BuildingFloor;
import com.f2z.gach.Map.Entity.PlaceSource;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminPlaceRequestDTO {
    private PlaceSource place;
    private List<BuildingFloor> buildingFloors;
    private MultipartFile mainFile;
    private MultipartFile thumbnailFile;
    private MultipartFile ARFile;

    public AdminPlaceRequestDTO(PlaceSource place) {
        this.place = place;
        this.buildingFloors = new ArrayList<>();
        this.mainFile = null;
        this.thumbnailFile = null;
        this.ARFile = null;
    }

    public static AdminPlaceRequestDTO toPlaceRequestDTO(PlaceSource place, List<BuildingFloor> buildingFloors){
        return AdminPlaceRequestDTO.builder()
                .place(place)
                .buildingFloors(buildingFloors).build();
    }
}
