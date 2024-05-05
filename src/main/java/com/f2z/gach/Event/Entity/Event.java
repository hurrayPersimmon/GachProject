package com.f2z.gach.Event.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
}
