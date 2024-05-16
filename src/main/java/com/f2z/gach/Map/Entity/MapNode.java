package com.f2z.gach.Map.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@DynamicUpdate
public class MapNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nodeId;

    @Column(unique = true)
    private String nodeName;
    private Double nodeLatitude;
    private Double nodeLongitude;
    private Double nodeAltitude;

    public void update(MapNode node) {
        this.nodeName = node.getNodeName();
        this.nodeLatitude = node.getNodeLatitude();
        this.nodeLongitude = node.getNodeLongitude();
        this.nodeAltitude = node.getNodeAltitude();
    }

    public static MapNode toRouteEntity(PlaceSource placeSource) {
        return MapNode.builder()
                .nodeLatitude(placeSource.getPlaceLatitude())
                .nodeLongitude(placeSource.getPlaceLongitude())
                .build();
    }
}
