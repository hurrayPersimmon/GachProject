package com.f2z.gach.Event.Repository;

import com.f2z.gach.Event.DTO.EventResponseDTO;
import com.f2z.gach.Event.Entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventEndDateAfter(LocalDate eventEndDate);

    boolean existsByEventName(String eventName);

    Event findByEventId(Integer id);

    Page<Event> findAllBy(Pageable pageable);
}
