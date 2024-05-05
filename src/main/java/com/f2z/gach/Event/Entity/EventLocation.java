package com.f2z.gach.Event.Entity;

import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Event eventName;
    private String eventPlaceName;
    private Double eventLatitude;
    private Double eventLongitude;
}
