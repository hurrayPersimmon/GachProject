package com.f2z.gach.History.DTO;


import com.f2z.gach.EnumType.Satisfaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;


public class HistoryRequestDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserHistoryRequestDTO{
        private Integer historyId;
        private Long userId;
        private Integer guestId;
        private String route;
        private Time totalTime;
        private Double temperature;
        private Double rainPrecipitation;
        private Integer rainPrecipitationProbability;
        private String departures;
        private String arrivals;
        private Satisfaction satisfactionRoute;
        private Satisfaction satisfactionTime;
        private LocalDateTime createDt;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class HistoryLineTimeRequestDTO{
        private Integer lineHistoryId;
        private Integer HistoryCode;
        private Long userCode;
        private Integer guestCode;
        private Integer lineIndex;
        private Integer lineCode;
        private Time lineTime;
        private Double lineVelocity;
    }

}
