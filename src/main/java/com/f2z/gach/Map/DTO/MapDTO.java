package com.f2z.gach.Map.DTO;

import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Map.Entity.BuildingFloor;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Entity.PlaceSource;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@ToString
@Builder
@AllArgsConstructor
public class MapDTO {


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @ToString
    @DynamicUpdate
    public static class MapNodeDTO{
        private Integer nodeId;
        @NotBlank(message = "노드 이름을 입력해주세요.")
        private String nodeName;

        @NotNull(message = "위도를 입력해주세요.")
        private Double nodeLatitude;
        @NotNull(message = "경도를 입력해주세요.")
        private Double nodeLongitude;
        @NotNull(message = "고도를 입력해주세요.")
        private Double nodeAltitude;

        public MapNode toEntity() {
            return MapNode.builder()
                    .nodeId(nodeId)
                    .nodeName(nodeName)
                    .nodeLatitude(nodeLatitude)
                    .nodeLongitude(nodeLongitude)
                    .nodeAltitude(nodeAltitude)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    public static class MapLineDTO {
        private Integer lineId;
        @NotBlank(message = "간선 이름을 입력해주세요.")
        private String lineName;
        @NotBlank(message = "노드를 지정해주세요.")
        private String nodeNameFirst;
        @NotBlank(message = "노드를 지정해주세요.")
        private String nodeNameSecond;

        private Integer nodeFirstId;
        private Integer nodeSecondId;

        private MapNode nodeFirst;
        private MapNode nodeSecond;

        public MapLineDTO toEntity(MapLineDTO mapLineDTO) {
            return MapLineDTO.builder()
                    .lineName(mapLineDTO.getLineName())
                    .nodeFirstId(mapLineDTO.getNodeFirstId())
                    .nodeSecondId(mapLineDTO.getNodeSecondId())
                    .build();
        }

        public MapLine toSaveEntity(String division, MapNode nodeFirst, MapNode nodeSecond) {
            Double weightShortest = getDistance(nodeFirst, nodeSecond);
            double deltaAltitude = (nodeSecond.getNodeAltitude() - nodeFirst.getNodeAltitude());
            Double weightOptimal = Math.toDegrees(Math.atan2(deltaAltitude, weightShortest));

            return MapLine.builder()
                    .lineName(lineName + division)
                    .nodeFirst(nodeFirst)
                    .nodeSecond(nodeSecond)
                    .weightShortest(weightShortest)
                    .weightOptimal(weightOptimal)
                    .build();
        }

        public static double getDistance(MapNode nodeFirst, MapNode nodeSecond){
            final double R = 6371.01;
            double dLat = Math.toRadians(nodeSecond.getNodeLatitude() - nodeFirst.getNodeLatitude());
            double dLon = Math.toRadians(nodeSecond.getNodeLongitude() - nodeFirst.getNodeLongitude());
            double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(nodeFirst.getNodeLatitude())) * Math.cos(Math.toRadians(nodeSecond.getNodeLatitude())) * Math.pow(Math.sin(dLon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return R * c;
        }


    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MapLineListStructure {
        private Integer lineId;
        private String lineName;
        private String nodeNameFirst;
        private String nodeNameSecond;
        private Double weightShortest;
        private Double weightOptimal;

        public static MapLineListStructure toMapLineListStructure(MapLine mapLine) {
            return MapLineListStructure.builder()
                    .lineId(mapLine.getLineId())
                    .lineName(mapLine.getLineName())
                    .nodeNameFirst(mapLine.getNodeFirst().getNodeName())
                    .nodeNameSecond(mapLine.getNodeSecond().getNodeName())
                    .weightShortest(mapLine.getWeightShortest())
                    .weightOptimal(mapLine.getWeightOptimal())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MapLineList {
        List<MapLineListStructure> lineList;
        Integer totalPage;
        Long totalElements;
    }

    public static MapLineList toMapLineList(Page<MapLine> linePage, List<MapLineListStructure> lineList){
        return MapLineList.builder()
                .lineList(lineList)
                .totalPage(linePage.getTotalPages())
                .totalElements(linePage.getTotalElements())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MapNodeListStructure {
        private Integer nodeId;
        private String nodeName;
        private Double nodeLatitude;
        private Double nodeLongitude;
        private Double nodeAltitude;

        public static MapNodeListStructure toMapNodeListStructure(MapNode mapNode) {
            return MapNodeListStructure.builder()
                    .nodeId(mapNode.getNodeId())
                    .nodeName(mapNode.getNodeName())
                    .nodeLatitude(mapNode.getNodeLatitude())
                    .nodeLongitude(mapNode.getNodeLongitude())
                    .nodeAltitude(mapNode.getNodeAltitude())
                    .build();
        }
    }
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MapNodeList {
        List<MapNodeListStructure> nodeList;
        Integer totalPage;
        Long totalElements;
    }

    public static MapNodeList toMapNodeList(Page<MapNode> nodePage, List<MapNodeListStructure> nodeList){
        return MapNodeList.builder()
                .nodeList(nodeList)
                .totalPage(nodePage.getTotalPages())
                .totalElements(nodePage.getTotalElements())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class PlaceSourceDTO {
        private Integer placeId;
        private String placeName;
        private String placeCategory;
        private Double placeLatitude;
        private Double placeLongitude;
        private Double placeAltitude;
        private String placeSummary;
        private Double buildingHeight;
        private List<BuildingFloor> buildingFloors = new ArrayList<>();
        private String mainImageName;
        private String mainImagePath;
        private String thumbnailImageName;
        private String thumbnailImagePath;
        private String arImageName;
        private String arImagePath;
        private MultipartFile mainFile;
        private MultipartFile thumbnailFile;
        private MultipartFile ARFile;

        public static PlaceSource toEntity(MapDTO.PlaceSourceDTO placeSourceDTO) {
            return PlaceSource.builder()
                    .placeId(placeSourceDTO.getPlaceId()!=null? placeSourceDTO.getPlaceId() : null)
                    .placeName(placeSourceDTO.getPlaceName())
                    .placeCategory(PlaceCategory.valueOf(placeSourceDTO.getPlaceCategory()))
                    .placeLatitude(placeSourceDTO.getPlaceLatitude())
                    .placeLongitude(placeSourceDTO.getPlaceLongitude())
                    .placeAltitude(placeSourceDTO.getPlaceAltitude())
                    .placeSummary(placeSourceDTO.getPlaceSummary())
                    .buildingHeight(placeSourceDTO.getBuildingHeight())
                    .mainImageName(placeSourceDTO.getMainImageName())
                    .mainImagePath(placeSourceDTO.getMainImagePath())
                    .thumbnailImageName(placeSourceDTO.getThumbnailImageName())
                    .thumbnailImagePath(placeSourceDTO.getThumbnailImagePath())
                    .arImageName(placeSourceDTO.getArImageName())
                    .arImagePath(placeSourceDTO.getArImagePath())
                    .build();
        }
    }




}
