package com.f2z.gach.Map.Repositories;

import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Map.Entities.BuildingInfo;
import com.f2z.gach.Map.Entities.PlaceSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceSourceRepository extends JpaRepository<PlaceSource, Integer> {
    List<PlaceSource> findAllByPlaceCategory(PlaceCategory placeCategory);
    PlaceSource findByPlaceId(Integer placeId);
}
