package com.f2z.gach.Event.DTO;

import com.f2z.gach.Event.Entity.EventLocation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class AdminEventRequestDTO {
    private EventDTO eventDTO;
    private List<EventLocationDTO> locations;
    private MultipartFile file;
}
