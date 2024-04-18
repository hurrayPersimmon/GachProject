package com.f2z.gach.Map.Repositories;

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
public class MapLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lineId;
    private String lineName;
    private Integer nodeCodeFirst;
    private String nodeNameFirst;
    private Integer nodeCodeSecond;
    private String nodeNameSecond;
    private Double weightShortest;
    private Double weightOptimal;


}
