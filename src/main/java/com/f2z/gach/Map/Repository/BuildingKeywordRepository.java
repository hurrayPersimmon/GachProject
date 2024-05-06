package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.BuildingKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingKeywordRepository extends JpaRepository<BuildingKeyword, Integer> {
    BuildingKeyword findByProfessorNameContaining(String target);
    BuildingKeyword findByDepartmentContaining(String target);
    BuildingKeyword findByCollegeContaining(String target);
    BuildingKeyword findByKeywordId(Integer keywordId);
}
