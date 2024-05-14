package com.f2z.gach.Event.DTO;

import com.f2z.gach.Event.Entity.Event;
import com.f2z.gach.Event.Entity.EventLocation;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdminEventRequestDTO {
        private Event event;
        private List<EventLocation> locations;
        private MultipartFile file;

        public static AdminEventRequestDTO toEventRequestDTO(Event event, List<EventLocation> locations){
            return AdminEventRequestDTO.builder()
                    .event(event)
                    .locations(locations)
                    .build();
        }
    }



