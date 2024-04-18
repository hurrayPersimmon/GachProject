package com.f2z.gach.Map.Repositories;

import com.f2z.gach.EnumType.College;
import com.f2z.gach.EnumType.Departments;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class BuildingKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
