package com.f2z.gach.Event.Entity;

import com.f2z.gach.Event.DTO.EventLocationDTO;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public void update(EventLocation eventLocation) {
        this.eventName = eventLocation.getEventName();
        this.eventPlaceName = eventLocation.getEventPlaceName();
        this.eventAltitude = eventLocation.getEventAltitude();
        this.eventLatitude = eventLocation.getEventLatitude();
        this.eventLongitude = eventLocation.getEventLongitude();
    }

    public static EventLocation updateEventLocation(EventLocation eventLocation, Event event) {
        return EventLocation.builder()
                .eventLocationId(eventLocation.getEventLocationId() != null ? eventLocation.getEventLocationId() : null)
                .eventPlaceName(eventLocation.getEventPlaceName())
                .eventAltitude(eventLocation.getEventAltitude())
                .eventLatitude(eventLocation.getEventLatitude())
                .eventLongitude(eventLocation.getEventLongitude())
                .eventName(event.getEventName())
                .event(event)
                .build();

    }


}
