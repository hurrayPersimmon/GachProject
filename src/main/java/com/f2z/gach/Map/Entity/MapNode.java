package com.f2z.gach.Map.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class MapNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nodeId;
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
}
