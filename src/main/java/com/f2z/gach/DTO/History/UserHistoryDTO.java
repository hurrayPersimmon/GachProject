package com.f2z.gach.DTO.History;

import com.f2z.gach.EnumType.Satisfaction;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserHistoryDTO {
    private Integer historyId;
    private Timestamp historyCreatedAt;
    private Long userCode;
    private Integer guestCode;
    private String route;
    private Time totalTime;
    private Double temperature;
    private Double rainPrecipitation;
    private Integer rainPrecipitationProbability;
    private String departures;
    private String arrivals;
    private Satisfaction satisfactionRoute;
    private Satisfaction satisfactionTime;




}
