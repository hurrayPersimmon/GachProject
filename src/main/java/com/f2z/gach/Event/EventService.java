package com.f2z.gach.Event;

import com.f2z.gach.Event.DTOs.EventResponseDTO;
import com.f2z.gach.Event.Entities.Event;
import com.f2z.gach.Event.Entities.EventLocation;
import com.f2z.gach.Response.ResponseEntity;

import java.util.List;

public interface EventService {
    ResponseEntity<EventResponseDTO.EventList> getEventList();

    ResponseEntity<List<EventLocation>> getEventLocationByEventCode(Integer eventCode);
}
