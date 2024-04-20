package com.f2z.gach.Map.DTOs.Responses;

import com.f2z.gach.Map.Entities.BuildingFloor;
import com.f2z.gach.Map.Entities.PlaceSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondPlaceInfoStructure {
        private Integer placeId;
        private String placeName;
        private String thumbnailImagePath;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondPlaceList {
        private List<respondPlaceInfoStructure> placeList;
    }

    public static respondPlaceInfoStructure toPlaceInfoStructure(PlaceSource place) {
        return respondPlaceInfoStructure.builder()
                .placeId(place.getPlaceId())
                .placeName(place.getPlaceName())
                .thumbnailImagePath(place.getThumbnailImagePath())
                .build();
    }

    public static respondPlaceList toRespondPlaceList(List<PlaceSource> places) {
        List<respondPlaceInfoStructure> placeList = places
                .stream()
                .map(PlaceResponseDTO::toPlaceInfoStructure)
                .collect(Collectors.toList());

        return respondPlaceList.builder()
                .placeList(placeList)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class toRespondBuildingInfo {
        private Integer placeId;
        private String placeName;
        private String thumbnailImagePath;
        private List<toRespondBuildingInfoStructure> buildingFloors;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class toRespondBuildingInfoStructure {
        private String buildingFloor;
        private String buildingFloorInfo;
    }

    public static toRespondBuildingInfoStructure toBuildingInfoStructure(BuildingFloor buildingFloor) {
        return toRespondBuildingInfoStructure.builder()
                .buildingFloor(buildingFloor.getBuildingFloor())
                .buildingFloorInfo(buildingFloor.getBuildingFloorInfo())
                .build();
    }

    public static List<toRespondBuildingInfoStructure> toBuildingInfoStructureList(List<BuildingFloor> buildingFloors) {
        return buildingFloors
                .stream()
                .map(PlaceResponseDTO::toBuildingInfoStructure)
                .collect(Collectors.toList());
    }

    public static toRespondBuildingInfo toRespondBuildingInfo(PlaceSource place, List<toRespondBuildingInfoStructure> buildingFloors) {
        return toRespondBuildingInfo.builder()
                .placeId(place.getPlaceId())
                .placeName(place.getPlaceName())
                .thumbnailImagePath(place.getThumbnailImagePath())
                .buildingFloors(buildingFloors)
                .build();
    }
}
