package com.f2z.gach.Event.Repositories;

import com.f2z.gach.Event.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventEndDateAfter(LocalDate eventEndDate);

    boolean existsByEventName(String eventName);
}
