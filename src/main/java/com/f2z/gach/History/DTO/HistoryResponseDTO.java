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
    public static class UserHistoryListStructure {
        Integer historyId;
        Long userId;
        Time totalTime;
        LocalDateTime createDt;
        String arrivals;
        String departures;
        Integer satisfactionRoute;
        Integer satisfactionTime;
    }

    public static UserHistoryListStructure toUserHistoryList(UserHistory history) {
        return UserHistoryListStructure.builder()
                .historyId(history.getHistoryId())
                .userId(history.getUser().getUserId())
                .totalTime(history.getTotalTime())
                .createDt(history.getCreateDt())
                .departures(history.getDepartures())
                .arrivals(history.getArrivals())
                .satisfactionRoute(history.getSatisfactionRoute().ordinal()+1)
                .satisfactionTime(history.getSatisfactionTime().ordinal()+1)
                .build();
    }

    public static List<UserHistoryListStructure> toUserHistoryListResponseDTO(List<UserHistory> historyList) {
        return historyList.stream().map(HistoryResponseDTO::toUserHistoryList).toList();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondSuccess {
        Integer historyId;
        Long userId;
    }

    public static respondSuccess toRespondSuccess(UserHistory history) {
        return respondSuccess.builder()
                .historyId(history.getHistoryId())
                .userId(history.getUser().getUserId())
                .build();
    }

//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Getter
//    public static class UserHistoryList {
//        List<HistoryResponseDTO> HistoryList;
//        Integer listSize;
//        Integer totalPage;
//        Long totalElements;
//        Boolean firstPage;
//        Boolean lastPage;
//    }
//
//    public static UserHistoryList toUserHistoryResponseList(Page<UserHistory> historyPages, List<HistoryResponseDTO> historyList) {
//        return UserHistoryList.builder()
//                .HistoryList(historyList)
//                .listSize(historyList.size())
//                .totalPage(historyPages.getTotalPages())
//                .totalElements(historyPages.getTotalElements())
//                .firstPage(historyPages.isFirst())
//                .lastPage(historyPages.isLast())
//                .build();
//    }


}
