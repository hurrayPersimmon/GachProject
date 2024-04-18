package com.f2z.gach.DataGetter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class nodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nodeId;
    private String nodeName;
    private Double nodeLatitude;
    private Double nodeLongitude;
    private Double nodeAltitude;
}