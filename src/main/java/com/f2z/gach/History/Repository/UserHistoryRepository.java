package com.f2z.gach.History.Repository;

import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.Map.Entity.MapNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    List<UserHistory> findAllByUser_userId(Long userId);
    @Query(value = "SELECT nodeId, COUNT(nodeId) AS frequency FROM (" +
            "SELECT Arrivals AS nodeId FROM UserHistory " +
            "UNION ALL " +
            "SELECT Departures AS nodeId FROM UserHistory) AS all_nodes " +
            "GROUP BY nodeId " +
            "ORDER BY frequency DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopMapNodes(@Param("limit") Integer limit);
}
