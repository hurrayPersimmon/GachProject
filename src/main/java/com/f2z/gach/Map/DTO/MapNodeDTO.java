package com.f2z.gach.Map.DTO;

import com.f2z.gach.Map.Entity.MapNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MapNodeDTO {
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
