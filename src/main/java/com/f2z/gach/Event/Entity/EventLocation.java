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
    private String eventName;
    private String eventPlaceName;
    private Double eventAltitude;
    private Double eventLatitude;
    private Double eventLongitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
