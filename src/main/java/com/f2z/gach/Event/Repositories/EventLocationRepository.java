package com.f2z.gach.Event.Repositories;

import com.f2z.gach.Event.Entities.EventLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLocationRepository extends JpaRepository<EventLocation, Integer> {
    List<EventLocation> findAllByEventCode(Integer eventCode);
}
