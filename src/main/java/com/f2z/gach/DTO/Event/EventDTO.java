package com.f2z.gach.DTO.Event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventDTO {
    private Integer eventId;
    private String eventName;
    private String eventLink;
    private Date eventStartDate;
    private Date eventEndDate;
    private String eventInfo;
    private String eventImageName;
    private String eventImagePath;

}