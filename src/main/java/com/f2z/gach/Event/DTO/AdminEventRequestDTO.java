package com.f2z.gach.Event.DTO;

import com.f2z.gach.Event.Entity.Event;
import com.f2z.gach.Event.Entity.EventLocation;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminEventRequestDTO {
        private Event event;
        private List<EventLocation> locations;
        private MultipartFile file;

        public AdminEventRequestDTO(Event event) {
            this.event = event;
            this.locations = new ArrayList<>();
        }

        public static AdminEventRequestDTO toEventRequestDTO(Event event, List<EventLocation> locations){
            return AdminEventRequestDTO.builder()
                    .event(event)
                    .locations(locations)
                    .build();
        }
    }



