package com.f2z.gach.History.Entity;

import com.f2z.gach.Map.Entity.MapLine;
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
public class HistoryLineTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lineHistoryId;
    private Integer lineIndex;
    private Time lineTime;
    private Double lineVelocity;

    //shortest, optimal 넣기.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historyId")
    private UserHistory userHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestId")
    private UserGuest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineId")
    private MapLine mapLine;



}
