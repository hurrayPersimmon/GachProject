package com.f2z.gach.Event.DTO;

import com.f2z.gach.Event.Entity.Event;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventDTO {
    private Integer eventId;
    private String eventName;
    private String eventLink;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private String eventInfo;
    private String eventImageName;
    private String eventImagePath;

    public Event toEntity() {
        return Event.builder()
                .eventId(eventId)
                .eventName(eventName)
                .eventLink(eventLink)
                .eventStartDate(eventStartDate)
                .eventEndDate(eventEndDate)
                .eventInfo(eventInfo)
                .eventImageName(eventImageName)
                .eventImagePath(eventImagePath)
                .build();
    }
}
