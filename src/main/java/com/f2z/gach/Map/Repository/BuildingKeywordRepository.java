package com.f2z.gach.Map.Repository;

import com.f2z.gach.Map.Entity.BuildingKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingKeywordRepository extends JpaRepository<BuildingKeyword, Integer> {
    List<BuildingKeyword> findAllByBuildingNameContaining(String target);
    List<BuildingKeyword> findAllByProfessorNameContaining(String target);
    List<BuildingKeyword> findAllByDepartmentContaining(String target);
    List<BuildingKeyword> findAllByCollegeContaining(String target);
    BuildingKeyword findByKeywordId(Integer keywordId);
}
