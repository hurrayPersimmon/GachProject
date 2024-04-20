package com.f2z.gach.Map.Repositories;

import com.f2z.gach.Map.Entities.BuildingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingFloorRepository extends JpaRepository<BuildingFloor, Integer> {
    List<BuildingFloor> findAllByBuildingCode(Integer buildingCode);
}
