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
