package com.f2z.gach.DTO.Map;

import com.f2z.gach.Entity.EnumType.College;
import com.f2z.gach.Entity.EnumType.Departments;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BuildingKeywordDTO {
    private Integer keywordId;
    private Integer buildingCode;
    private String buildingName;
    @Enumerated(EnumType.STRING)
    private College college;
    @Enumerated(EnumType.STRING)
    private Departments departments;
    private String professorName;
    private String professorClass;



}
