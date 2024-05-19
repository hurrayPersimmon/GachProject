package com.f2z.gach.DataGetter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface dataRepository extends JpaRepository<dataEntity, Long> {

    List<dataEntity> findByIdBetween(Long startIndex, Long endIndex);

}
