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

import java.time.LocalDateTime;
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

    @Query(value = "SELECT nodeId, COUNT(nodeId) AS frequency FROM (" +
            "SELECT Arrivals AS nodeId FROM UserHistory " +
            "UNION ALL " +
            "SELECT Departures AS nodeId FROM UserHistory) AS all_nodes " +
            "GROUP BY nodeId " +
            "ORDER BY frequency ASC " + // 빈도수가 낮은 순으로 정렬
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findBottomMapNodes(@Param("limit") Integer limit);

    @Query("SELECT new java.sql.Date(FUNCTION('DATE', uh.createDt)), AVG(uh.satisfactionRoute) " +
            "FROM UserHistory uh " +
            "WHERE uh.createDt BETWEEN :startOfDay AND :endOfDay " +
            "GROUP BY FUNCTION('DATE', uh.createDt)")
    List<Object[]> findAverageSatisfactionRouteByDateRange(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}
