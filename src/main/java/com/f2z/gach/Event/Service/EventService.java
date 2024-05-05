package com.f2z.gach.Event.Service;

import com.f2z.gach.Event.DTO.EventResponseDTO;
import com.f2z.gach.Event.Entity.EventLocation;
import com.f2z.gach.Response.ResponseEntity;

import java.util.List;

public interface EventService {
    ResponseEntity<EventResponseDTO.EventList> getEventList();

    ResponseEntity<List<EventLocation>> getEventLocationByEventCode(Integer eventCode);
}
