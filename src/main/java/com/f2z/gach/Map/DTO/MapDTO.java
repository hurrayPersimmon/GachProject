package com.f2z.gach.Map.DTO;

import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;


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
        private Integer nodeCodeFirst;
        @NotBlank(message = "노드를 지정해주세요.")
        private String nodeNameFirst;
        private Integer nodeCodeSecond;
        @NotBlank(message = "노드를 지정해주세요.")
        private String nodeNameSecond;
        private Double weightShortest;
        private Double weightOptimal;

        public MapLine toEntity() {
            return MapLine.builder()
                    .lineName(lineName)
                    .nodeNameFirst(nodeNameFirst)
                    .nodeNameSecond(nodeNameSecond)
                    .build();
        }

        public MapLine toSaveEntity(String division, MapNode nodeFirst, MapNode nodeSecond) {
            Double weightShortest = getDistance(nodeFirst, nodeSecond);
            double deltaAltitude = (nodeSecond.getNodeAltitude() - nodeFirst.getNodeAltitude());

            Double weightOptimal = Math.toDegrees(Math.atan2(deltaAltitude, weightShortest));

            return MapLine.builder()
                    .lineName(lineName + division)
                    .nodeNameFirst(nodeFirst.getNodeName())
                    .nodeCodeFirst(nodeFirst.getNodeId())
                    .nodeNameSecond(nodeSecond.getNodeName())
                    .nodeCodeSecond(nodeSecond.getNodeId())
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

}
