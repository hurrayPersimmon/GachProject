package com.f2z.gach.DTO.Map;

import com.f2z.gach.Map.Entity.MapLine;
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
@Builder
public class MapLineDTO {
    private Integer lineId;
    private String lineName;
    private Integer nodeCodeFirst;
    private String nodeNameFirst;
    private Integer nodeCodeSecond;
    private String nodeNameSecond;
    private Double weightShortest;
    private Double weightOptimal;


    public MapLine toEntity() {
        return MapLine.builder()
                .lineId(lineId)
                .lineName(lineName)
                .nodeCodeFirst(nodeCodeFirst)
                .nodeNameFirst(nodeNameFirst)
                .nodeCodeSecond(nodeCodeSecond)
                .nodeNameSecond(nodeNameSecond)
                .weightShortest(weightShortest)
                .weightOptimal(weightOptimal)
                .build();
    }
}
