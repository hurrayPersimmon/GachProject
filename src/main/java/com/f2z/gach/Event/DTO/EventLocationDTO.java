package com.f2z.gach.Event.DTO;

import com.f2z.gach.Event.Entity.Event;
import com.f2z.gach.Event.Entity.EventLocation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EventLocationDTO {
    private String eventPlaceName;
    private Double eventAltitude;
    private Double eventLatitude;
    private Double eventLongitude;

    public static EventLocation toEventLocation(EventLocationDTO eventLocationDTO, Event event) {
        return EventLocation.builder()
                .eventPlaceName(eventLocationDTO.getEventPlaceName())
                .eventAltitude(eventLocationDTO.getEventAltitude())
                .eventLatitude(eventLocationDTO.getEventLatitude())
                .eventLongitude(eventLocationDTO.getEventLongitude())
                .eventName(event.getEventName())
                .event(event)
                .build();

    }
}
