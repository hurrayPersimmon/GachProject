package com.f2z.gach.History.Entity;

import com.f2z.gach.Config.BaseTimeEntity;
import com.f2z.gach.EnumType.Satisfaction;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.User.Entity.User;
import com.f2z.gach.User.Entity.UserGuest;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class UserHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;
    private String route;
    private Integer totalTime;
    private Double temperature;
    private Double rainPrecipitation;
    private Integer rainPrecipitationProbability;
    private Satisfaction satisfactionRoute;
    private Satisfaction satisfactionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestId")
    private UserGuest guest;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "Departures", referencedColumnName = "nodeName")
    private MapNode departures;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "Arrivals", referencedColumnName = "nodeName")
    private MapNode arrivals;


}
