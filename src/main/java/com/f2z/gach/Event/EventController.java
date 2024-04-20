package com.f2z.gach.Event;

import com.f2z.gach.Event.DTOs.EventResponseDTO;
import com.f2z.gach.Event.Entities.Event;
import com.f2z.gach.Event.Entities.EventLocation;
import com.f2z.gach.Response.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @GetMapping("/list")
    public ResponseEntity<EventResponseDTO.EventList> getEventList() throws Exception {
        return eventService.getEventList();
    }

    @GetMapping("/{eventCode}")
    public ResponseEntity<List<EventLocation>> getEventLocationByEventCode(@PathVariable Integer eventCode) {
        return eventService.getEventLocationByEventCode(eventCode);
    }
}
