package com.f2z.gach.Map.Repository;

import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Map.Entity.PlaceSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceSourceRepository extends JpaRepository<PlaceSource, Integer> {
    List<PlaceSource> findAllByPlaceCategory(PlaceCategory placeCategory);
    PlaceSource findByPlaceId(Integer placeId);

    @Query("SELECT p FROM PlaceSource p WHERE p.placeName LIKE %?1%")
    List<PlaceSource> findPlaceSourcesByPlaceNameContaining(String placeName);
    List<PlaceSource> findPlaceSourcesByPlaceCategory(PlaceCategory placeCategoryContaining);
}

