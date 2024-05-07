package com.f2z.gach.Event.DTO;

import com.f2z.gach.Event.Entity.Event;
import com.f2z.gach.Event.Entity.EventLocation;
import com.f2z.gach.Inquiry.DTO.InquiryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EventResponseDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EventListStructure {
        private Integer eventId;
        private String eventName;
        private String eventLink;
        private String eventInfo;
        private String eventImagePath;
    }

    public static EventListStructure toEventListStructure(Event event) {
        return EventListStructure.builder()
                .eventId(event.getEventId())
                .eventName(event.getEventName())
                .eventLink(event.getEventLink())
                .eventInfo(event.getEventInfo())
                .eventImagePath(event.getEventImagePath())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EventList {
        List<EventListStructure> eventList;
    }

    public static EventList toEventList(List<Event> events) {
        List<EventListStructure> eventList = events
                .stream()
                //람다식, event -> toEventListStructure(event)와 같은 의미
                .map(EventResponseDTO::toEventListStructure)
                .collect(Collectors.toList());

        return EventList.builder()
                .eventList(eventList)
                .build();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EventLocationResponse {
        private Integer eventInfoId;
        private String eventName;
        private Double eventLatitude;
        private Double eventLongitude;
        private Double eventAltitude;
        private Integer eventId;
        private String eventPlaceName;
    }

    public static EventLocationResponse toEventLocationResponse(EventLocation eventLocation) {
        return EventLocationResponse.builder()
                .eventInfoId(eventLocation.getEventInfoId())
                .eventName(eventLocation.getEventName())
                .eventLatitude(eventLocation.getEventLatitude())
                .eventLongitude(eventLocation.getEventLongitude())
                .eventAltitude(eventLocation.getEventAltitude())
                .eventId(eventLocation.getEvent().getEventId())
                .eventPlaceName(eventLocation.getEventPlaceName())
                .build();
    }

    public static List<EventLocationResponse> toEventLocationList(List<EventLocation> eventLocationResponses) {
        return eventLocationResponses
                .stream()
                .map(EventResponseDTO::toEventLocationResponse)
                .collect(Collectors.toList());
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AdminEventListStructure {
        private Integer eventId;
        private String eventName;
        private LocalDate eventStartDate;
        private LocalDate eventEndDate;
        private String eventLink;
        private String eventImageName;

        public static AdminEventListStructure toAdminEventListStructure(Event event) {
            return AdminEventListStructure.builder()
                    .eventId(event.getEventId())
                    .eventName(event.getEventName())
                    .eventStartDate(event.getEventStartDate())
                    .eventEndDate(event.getEventEndDate())
                    .eventLink(event.getEventLink())
                    .eventImageName(event.getEventImageName())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AdminEventList {
        List<AdminEventListStructure> adminEventList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean firstPage;
        Boolean lastPage;
    }

    public static AdminEventList toAdminEventList(Page<Event> events, List<AdminEventListStructure> eventList) {
        return AdminEventList.builder()
                .adminEventList(eventList)
                .listSize(eventList.size())
                .totalPage(events.getTotalPages())
                .totalElements(events.getTotalElements())
                .firstPage(events.isFirst())
                .lastPage(events.isLast())
                .build();
    }




}
