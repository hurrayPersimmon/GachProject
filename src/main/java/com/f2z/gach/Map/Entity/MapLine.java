package com.f2z.gach.Map.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class MapLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lineId;
    private String lineName;
    private Double weightShortest;
    private Double weightOptimal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeIdFirst", updatable = false, referencedColumnName = "nodeId")
    private MapNode nodeFirst;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeIdSecond", updatable = false, referencedColumnName = "nodeId")
    private MapNode nodeSecond;


}
