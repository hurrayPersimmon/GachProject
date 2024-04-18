package com.f2z.gach.DataGetter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface dataRepository extends JpaRepository<dataEntity, Long> {

}
