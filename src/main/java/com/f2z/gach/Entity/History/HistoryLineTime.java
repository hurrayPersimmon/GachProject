package com.f2z.gach.Entity.History;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class HistoryLineTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lineHistoryId;
    private Integer HistoryCode;
    private Long userCode;
    private Integer guestCode;
    private Integer lineIndex;
    private Integer lineCode;
    private Time lineTime;
    private Double lineVelocity;


}
