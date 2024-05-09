package com.f2z.gach.Map.Entity;

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
    private String placeName;
    @Enumerated(EnumType.STRING)
    private College college;
    @Enumerated(EnumType.STRING)
    private Departments department;
    private String professorName;
    private String professorClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeId", updatable = false)
    private PlaceSource placeSource;



}
