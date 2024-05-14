package com.f2z.gach.Event.Repository;

import com.f2z.gach.Event.Entity.EventLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLocationRepository extends JpaRepository<EventLocation, Integer> {
    List<EventLocation> findAllByEvent_EventId(Integer eventId);
    EventLocation findByEventLocationId(Integer eventLocationId);

    void deleteEventLocationsByEvent_EventId(Integer eventId);

}
