package com.f2z.gach.History.DTO;


import com.f2z.gach.EnumType.Satisfaction;
import com.f2z.gach.History.Entity.HistoryLineTime;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Repository.MapLineRepository;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import com.f2z.gach.User.Entity.UserGuest;
import com.f2z.gach.User.Repository.UserGuestRepository;
import com.f2z.gach.User.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class HistoryRequestDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserHistoryRequestDTO{
        private Long userId;
        private Integer guestId;
        private Integer departure;
        private Integer arrival;
        private Satisfaction satisfactionRoute;
        private Satisfaction satisfactionTime;
        private Double temperature;
        private Double rainPrecipitation;
        private Integer rainPrecipitationProbability;
        private List<HistoryLineTimeRequestDTO> timeList;

        public static UserHistory toEntity(UserHistoryRequestDTO dto,
                                           UserRepository userRepository,
                                           UserGuestRepository userGuestRepository,
                                           MapNodeRepository mapNodeRepository){
            return UserHistory.builder()
                    .user(dto.getUserId() != null ? userRepository.findByUserId(dto.getUserId()) : null)
                    .guest(dto.getGuestId() != null ? userGuestRepository.findByGuestId(dto.getGuestId()) : null)
                    .route(dto.getTimeList().stream().map(time -> time.getFirstNodeId() + " -> " + time.getSecondNodeId()).reduce((a, b) -> a + " -> " + b).orElse(null))
                    .totalTime(dto.getTimeList().stream().mapToInt(HistoryLineTimeRequestDTO::getTime).sum())
                    .departures(mapNodeRepository.findByNodeId(dto.getDeparture()))
                    .arrivals(mapNodeRepository.findByNodeId(dto.getArrival()))
                    .satisfactionRoute(dto.getSatisfactionRoute())
                    .satisfactionTime(dto.getSatisfactionTime())
                    .temperature(dto.getTemperature())
                    .rainPrecipitation(dto.getRainPrecipitation())
                    .rainPrecipitationProbability(dto.getRainPrecipitationProbability())
                    .build();
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class HistoryLineTimeRequestDTO{
        private Integer firstNodeId;
        private Integer secondNodeId;
        private Integer time;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class lineHistoryDTO{
        private Integer lineTime;
        private Double lineVelocity;
        private Integer historyId;
        private Integer lineId;

        public static HistoryLineTime toEntity(UserHistory history,
                                               MapLine line,
                                               Integer lineTime,
                                               Double lineVelocity){
            return HistoryLineTime.builder()
                    .lineTime(lineTime)
                    .lineVelocity(lineVelocity)
                    .userHistory(history)
                    .mapLine(line)
                    .build();
        }
    }

}
