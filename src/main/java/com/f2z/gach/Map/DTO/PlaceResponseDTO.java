package com.f2z.gach.Map.DTO;

import com.f2z.gach.Map.Entity.BuildingFloor;
import com.f2z.gach.Map.Entity.PlaceSource;
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
        private List<respondPlaceInfoStructure> buildingList;
    }

    public static respondPlaceInfoStructure toPlaceInfoStructure(PlaceSource place) {
        return respondPlaceInfoStructure.builder()
                .placeId(place.getPlaceId())
                .placeName(place.getPlaceName())
                .thumbnailImagePath(place.getThumbnailImagePath())
                .build();
    }

    public static respondPlaceList toRespondPlaceList(List<PlaceSource> buildings) {
        List<respondPlaceInfoStructure> buildingList = buildings
                .stream()
                .map(PlaceResponseDTO::toPlaceInfoStructure)
                .collect(Collectors.toList());

        return respondPlaceList.builder()
                .buildingList(buildingList)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class toRespondBuildingInfo {
        private String placeName;
        private String placeSummary;
        private Double placeLatitude;
        private Double placeLongitude;
        private String mainImagePath;
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
                .placeName(place.getPlaceName())
                .placeSummary(place.getPlaceSummary())
                .placeLatitude(place.getPlaceLatitude())
                .placeLongitude(place.getPlaceLongitude())
                .mainImagePath(place.getMainImagePath())
                .buildingFloors(buildingFloors)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class placeLocationDTO {
            private String placeName;
            private Double placeLatitude;
            private Double placeLongitude;
            private Double placeAltitude;
    }

    public static placeLocationDTO toPlaceLocationDTO(PlaceSource place) {
        return placeLocationDTO.builder()
                .placeName(place.getPlaceName())
                .placeLatitude(place.getPlaceLatitude())
                .placeLongitude(place.getPlaceLongitude())
                .placeAltitude(place.getPlaceLongitude())
                .build();
    }

    public static List<placeLocationDTO> toPlaceLocationDTOList(List<PlaceSource> placeList) {
        return placeList
                .stream()
                .map(PlaceResponseDTO::toPlaceLocationDTO)
                .collect(Collectors.toList());
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondKeywordList {
        private Integer placeId;
        private String placeName;
        private String placeSummary;
    }

    public static respondKeywordList toKeywordList(PlaceSource place) {
        return respondKeywordList.builder()
                .placeId(place.getPlaceId())
                .placeName(place.getPlaceName())
                .placeSummary(place.getPlaceSummary())
                .build();
    }

    public static List<respondKeywordList> toKeywordList(List<PlaceSource> places) {
        return places
                .stream()
                .map(PlaceResponseDTO::toKeywordList)
                .collect(Collectors.toList());
    }


}
