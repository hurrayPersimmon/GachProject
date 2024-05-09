package com.f2z.gach.Event.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class EventLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventLocationId;
    private String eventName;
    private String eventPlaceName;
    private Double eventAltitude;
    private Double eventLatitude;
    private Double eventLongitude;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "event_id")
    private Event event;

    public void update(EventLocation eventLocation) {
        this.eventName = eventLocation.getEventName();
        this.eventPlaceName = eventLocation.getEventPlaceName();
        this.eventAltitude = eventLocation.getEventAltitude();
        this.eventLatitude = eventLocation.getEventLatitude();
        this.eventLongitude = eventLocation.getEventLongitude();
    }


}
