package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.BuildingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingFloorRepository extends JpaRepository<BuildingFloor, Integer> {
    List<BuildingFloor> findAllByPlaceSource_placeId(Integer placeId);

    void deleteAllByPlaceSource_PlaceId(Integer placeId);
}
