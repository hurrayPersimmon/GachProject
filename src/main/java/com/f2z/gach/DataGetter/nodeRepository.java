package com.f2z.gach.DataGetter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface nodeRepository extends JpaRepository<nodeEntity, Integer> {
    @Query("SELECT n FROM nodeEntity n WHERE n.nodeName = ?1")
    nodeEntity findByNodeName(String nodeName);
    nodeEntity findByNodeId(Integer nodeId);

}
