package com.f2z.gach.Log.Repository;

import com.f2z.gach.EnumType.LogLevel;
import com.f2z.gach.Log.Entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    Integer countAllByLogLevel(LogLevel logLevel);
    Integer countAllByCreateDtAndUrl(String url);
    @Query("SELECT COUNT(l) FROM Log l WHERE l.createDt >= :startOfDay AND l.createDt < :endOfDay AND l.httpMethod = :httpMethod AND l.logLevel = :logLevel AND l.url = :url")
    Integer countBySpecificConditions(@Param("startOfDay") LocalDateTime startOfDay,
                                      @Param("endOfDay") LocalDateTime endOfDay,
                                      @Param("httpMethod") String httpMethod,
                                      @Param("logLevel") LogLevel logLevel,
                                      @Param("url") String url);
    @Query("SELECT COUNT(l) FROM Log l WHERE l.createDt >= :startOfDay AND l.createDt < :endOfDay")
    Integer countLogsCreatedToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(l) FROM Log l WHERE l.createDt >= :startOfDay AND l.createDt < :endOfDay AND l.logLevel = :logLevel")
    Integer countByLogLevel(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay, @Param("logLevel") LogLevel logLevel);

}
