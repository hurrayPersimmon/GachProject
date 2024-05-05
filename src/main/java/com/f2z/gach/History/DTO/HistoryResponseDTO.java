package com.f2z.gach.History.DTO;

import com.f2z.gach.EnumType.Satisfaction;
import com.f2z.gach.History.Entity.UserHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HistoryResponseDTO {
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


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserHistoryList {
        List<HistoryResponseDTO> HistoryList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean firstPage;
        Boolean lastPage;
    }

    public static UserHistoryList toUserHistoryResponseList(Page<UserHistory> historyPages, List<HistoryResponseDTO> historyList) {
        return UserHistoryList.builder()
                .HistoryList(historyList)
                .listSize(historyList.size())
                .totalPage(historyPages.getTotalPages())
                .totalElements(historyPages.getTotalElements())
                .firstPage(historyPages.isFirst())
                .lastPage(historyPages.isLast())
                .build();
    }

    public static HistoryResponseDTO toUserHistoryListResponseDTO(UserHistory history) {
        return HistoryResponseDTO.builder()
                .historyId(history.getHistoryId())
                .userId(history.getUserId())
                .totalTime(history.getTotalTime())
                .departures(history.getDepartures())
                .createDt(history.getCreateDt())
                .arrivals(history.getArrivals())
                .satisfactionRoute(history.getSatisfactionRoute())
                .satisfactionTime(history.getSatisfactionTime())
                .build();
    }


}
