package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.MapNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapNodeRepository extends JpaRepository<MapNode, Integer> {
    boolean existsByNodeName(String nodeName);
    MapNode findByNodeId(Integer nodeId);

    boolean existsByNodeId(Integer nodeId);

    List<String> findALLNodeName();

    MapNode findByNodeName(String nodeNameFirst);
}
