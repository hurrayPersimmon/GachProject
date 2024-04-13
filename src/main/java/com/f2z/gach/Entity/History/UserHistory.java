package com.f2z.gach.Entity.History;

import com.f2z.gach.Entity.BaseTimeEntity;
import com.f2z.gach.Entity.EnumType.Authorization;
import com.f2z.gach.Entity.EnumType.Satisfaction;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UserHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;
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
