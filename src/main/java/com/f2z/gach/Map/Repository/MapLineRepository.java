package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapLineRepository extends JpaRepository<MapLine,Integer>{
    boolean existsByLineId(Integer lineId);
    void deleteByLineId(Integer lineId);
    List<MapLine> findAllByNodeFirst_NodeId(Integer nodeId);
    MapLine findLineIdByNodeFirst_NodeIdAndNodeSecond_NodeId(Integer nodeFirstId, Integer nodeSecondId);

}
