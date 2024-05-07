package com.f2z.gach.Event.Controller;

import com.f2z.gach.Event.DTO.EventResponseDTO;
import com.f2z.gach.Event.Entity.EventLocation;
import com.f2z.gach.Event.Service.EventService;
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
public class EventRestController {
    private final EventService eventService;

    @GetMapping("/list")
    public ResponseEntity<EventResponseDTO.EventList> getEventList() throws Exception {
        return eventService.getEventList();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<EventResponseDTO.EventLocationResponse>> getEventLocationByEventId(@PathVariable Integer eventId) {
        return eventService.getEventLocationByEventId(eventId);
    }
}
