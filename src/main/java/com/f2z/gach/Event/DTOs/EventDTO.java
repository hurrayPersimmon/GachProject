package com.f2z.gach.Event.DTOs;

import com.f2z.gach.Event.Entities.Event;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    private List<EventLocationDTO> locations;
    private MultipartFile file;

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
