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
@Builder
public class HistoryLineTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lineHistoryId;
    private Integer lineTime;
    private Double lineVelocity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historyId")
    private UserHistory userHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineId")
    private MapLine mapLine;



}
