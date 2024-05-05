package com.f2z.gach.Event.Service;

import com.f2z.gach.Event.DTO.EventResponseDTO;
import com.f2z.gach.Event.Entity.Event;
import com.f2z.gach.Event.Entity.EventLocation;
import com.f2z.gach.Event.Repository.EventLocationRepository;
import com.f2z.gach.Event.Repository.EventRepository;
import com.f2z.gach.Response.ResponseEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
