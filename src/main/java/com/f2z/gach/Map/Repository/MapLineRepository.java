package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapLineRepository extends JpaRepository<MapLine,Integer>{
    Boolean existsByLineName(String lineName);

    MapLine findByLineId(Integer lineId);

    boolean existsByNodeFirst_NodeNameAndNodeSecond_NodeName(String nodeNameFirst, String nodeNameSecond);

    boolean existsByLineId(Integer lineId);

    void deleteByLineId(Integer lineId);

    boolean existsByNodeFirst_NodeId(Integer placeId);

    List<MapLine> findAllByNodeFirst_NodeId(Integer nodeId);

}
