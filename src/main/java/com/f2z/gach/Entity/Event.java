package com.f2z.gach.Entity;

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
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    private String eventName;
    private String eventLink;
    private Date eventStartDate;
    private Date eventEndDate;
    private String eventInfo;
    private String eventImageName;
    private String eventImagePath;

}
