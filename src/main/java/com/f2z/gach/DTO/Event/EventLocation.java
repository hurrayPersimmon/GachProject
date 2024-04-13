package com.f2z.gach.DTO.Event;

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
public class EventLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventInfoId;
    private Integer eventCode;
    private String eventName;
    private String eventPlaceName;
    private Double eventLatitude;
    private Double eventLongitude;
    private Double eventAltitude;


}
