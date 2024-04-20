package com.f2z.gach.Event;

import com.f2z.gach.Event.DTOs.EventResponseDTO;
import com.f2z.gach.Event.Entities.Event;
import com.f2z.gach.Event.Entities.EventLocation;
import com.f2z.gach.Event.EventService;
import com.f2z.gach.Event.Repositories.EventLocationRepository;
import com.f2z.gach.Event.Repositories.EventRepository;
import com.f2z.gach.Response.ResponseEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventLocationRepository eventLocationRepository;
    @Override
    public ResponseEntity<EventResponseDTO.EventList> getEventList() {
        LocalDate currentDate = LocalDate.now();
        log.info("currentDate: " + currentDate);
        List<Event> eventList = eventRepository.findByEventEndDateAfter(currentDate);
        if(eventList.isEmpty()){
            return ResponseEntity.notFound(null);
        }
        else {
            return ResponseEntity.requestSuccess(EventResponseDTO.toEventList(eventList));
        }
    }

    @Override
    public ResponseEntity<List<EventLocation>> getEventLocationByEventCode(Integer eventCode) {
        List<EventLocation> eventLocationList = eventLocationRepository.findAllByEventCode(eventCode);
        if(eventLocationList.isEmpty()){
            return ResponseEntity.notFound(null);
        }
        else {
            return ResponseEntity.requestSuccess(eventLocationList);
        }
    }
}
