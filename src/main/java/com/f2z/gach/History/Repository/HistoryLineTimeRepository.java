package com.f2z.gach.History.Repository;

import com.f2z.gach.History.Entity.HistoryLineTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryLineTimeRepository extends JpaRepository<HistoryLineTime, Long> {

}
