package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Entity.PlaceSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MapNodeRepository extends JpaRepository<MapNode, Integer> {
    boolean existsByNodeName(String nodeName);
    MapNode findByNodeId(Integer nodeId);
    boolean existsByNodeId(Integer nodeId);
    MapNode findByNodeName(String nodeNameFirst);

    @Query("SELECT p FROM MapNode p WHERE p.nodeName LIKE %?1%")
    Page<MapNode> findAllByNodeNameContaining(String placeName, Pageable pageable);

}
