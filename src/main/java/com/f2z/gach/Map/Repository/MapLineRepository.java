package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.MapLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapLineRepository extends JpaRepository<MapLine,Integer>{
    Boolean existsByLineName(String lineName);
}
