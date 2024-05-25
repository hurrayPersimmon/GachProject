package com.f2z.gach.Map.Repository;

import com.f2z.gach.EnumType.College;
import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.Map.Entity.BuildingKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingKeywordRepository extends JpaRepository<BuildingKeyword, Integer> {

    @Query("SELECT b FROM BuildingKeyword b WHERE b.professorName LIKE %?1%")
    BuildingKeyword findByProfessorNameContaining(String target);
    Boolean existsByCollege(College college);
    Boolean existsByDepartment(Departments departments);
    List<BuildingKeyword> findAllByCollege(College collegeContaining);
    List<BuildingKeyword> findAllByDepartment(Departments departmentsContaining);
}
