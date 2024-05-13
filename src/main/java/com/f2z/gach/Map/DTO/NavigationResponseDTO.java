package com.f2z.gach.Map.DTO;

import com.f2z.gach.Map.Entity.MapNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NavigationResponseDTO {
    private String routeType;
    private Integer totalTime;
    private List<NodeDTO> nodeList;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AdminMapNode{
        private MapNode departures;
        private MapNode arrivals;
    }

    public static AdminMapNode toAdminMapNode(MapNode departures, MapNode arrivals){
        return AdminMapNode.builder()
                .departures(departures)
                .arrivals(arrivals)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AdminNodeList{
        private List<NodeDTO> shortestList;
        private List<NodeDTO> optimalList;
    }

    public static AdminNodeList toAdminNodeList(List<NodeDTO> shortestList, List<NodeDTO> optimalList){
        return AdminNodeList.builder()
                .shortestList(shortestList)
                .optimalList(optimalList)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NodeDTO{
        private Integer nodeId;
        private Double latitude;
        private Double longitude;
        private Double altitude;
    }

    public static NavigationResponseDTO toNavigationResponseDTO(String routeType, Integer totalTime, List<NodeDTO> nodeList){
        return NavigationResponseDTO.builder()
                .routeType(routeType)
                .totalTime(totalTime)
                .nodeList(nodeList)
                .build();
    }
}
