package com.f2z.gach.Event.Entity;

import com.f2z.gach.Event.DTO.EventDTO;
import com.f2z.gach.Event.DTO.EventResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    private String eventName;
    private String eventLink;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventEndDate;
    private String eventInfo;
    private String eventImageName;
    private String eventImagePath;

    public void update(Event event) {
        this.eventName = event.getEventName();
        this.eventLink = event.getEventLink();
        this.eventStartDate = event.getEventStartDate();
        this.eventEndDate = event.getEventEndDate();
        this.eventInfo = event.getEventInfo();
        if(event.getEventImageName() != null) this.eventImageName = event.getEventImageName();
        if(event.getEventImagePath() != null) this.eventImagePath = event.getEventImagePath();
    }
}
