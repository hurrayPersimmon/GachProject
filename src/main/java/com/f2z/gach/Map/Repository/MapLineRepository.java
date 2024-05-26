package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MapLineRepository extends JpaRepository<MapLine,Integer>{
    boolean existsByLineId(Integer lineId);
    void deleteByLineId(Integer lineId);
    List<MapLine> findAllByNodeFirst_NodeId(Integer nodeId);
    MapLine findByNodeFirstNodeIdAndNodeSecondNodeId(Integer nodeFirstId, Integer nodeSecondId);
    void deleteAllByNodeFirst_NodeId(Integer nodeId);
    void deleteAllByNodeSecond_NodeId(Integer nodeId);
    @Query("SELECT p FROM MapLine p WHERE p.lineName LIKE %?1%")
    Page<MapLine> findAllByLineNameContaining(String lineName, Pageable pageable);
}
